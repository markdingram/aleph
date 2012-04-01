;;   Copyright (c) Zachary Tellman. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file epl-v10.html at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.

(ns aleph.tcp
  (:use
    [lamina core trace]
    [aleph netty formats])
  (:import
    [java.nio.channels
     ClosedChannelException]))

(defn- wrap-tcp-channel [ch]
  (let [ch* (channel)]
    (join (map* bytes->channel-buffer ch*) ch)
    (splice ch ch*)))

(defn start-tcp-server [handler options]
  (let [server-name (or
                      (:name options)
                      (-> options :server :name)
                      "tcp-server")
        error-predicate (or
                          (:error-predicate options)
                          #(not (instance? ClosedChannelException %)))]
    (start-server
      server-name
      (fn [channel-group]
        (create-netty-pipeline server-name error-predicate channel-group
          :handler (server-message-handler
                     (fn [ch x]
                       (handler (wrap-tcp-channel ch) x)))))
      options)))

(defn tcp-client [options]
  (let [client-name (or
                      (:name options)
                      (-> options :client :name)
                      "tcp-client")
        error-predicate (or
                          (:error-predicate options)
                          #(not (instance? ClosedChannelException %)))]
    (run-pipeline nil
      {:error-handler (fn [_])}
      (fn [_]
        (create-client
          client-name
          (fn [channel-group]
            (create-netty-pipeline client-name error-predicate channel-group))
          options))
      wrap-tcp-channel)))
