(defproject org.clojars.markdingram/aleph "0.4.1-pr277"
  :description "a framework for asynchronous communication"
  :repositories {"jboss" "http://repository.jboss.org/nexus/content/groups/public/"
                 "sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :license {:name "MIT License"}
  :dependencies [[org.clojure/tools.logging "0.3.1" :exclusions [org.clojure/clojure]]
                 [io.netty/netty-all "4.1.0.CR3"]
                 [io.aleph/dirigiste "0.1.3"]
                 [manifold "0.1.4"]
                 [byte-streams "0.2.2"]
                 [potemkin "0.4.3"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.8.0"]
                                  [criterium "0.4.4"]]}}
  :codox {:src-dir-uri "https://github.com/ztellman/aleph/tree/master/"
          :src-linenum-anchor-prefix "L"
          :defaults {:doc/format :markdown}
          :include [aleph.tcp
                    aleph.udp
                    aleph.http
                    aleph.flow]
          :output-dir "doc"}
  :plugins [[codox "0.8.10"]
            [lein-jammin "0.1.1"]
            [ztellman/lein-cljfmt "0.1.10"]]
  :cljfmt {:indents {#".*" [[:inner 0]]}}
  :test-selectors {:default #(not
                               (some #{:benchmark :stress}
                                 (cons (:tag %) (keys %))))
                   :benchmark :benchmark
                   :stress :stress
                   :all (constantly true)}
  :jvm-opts ^:replace ["-server"
                       "-XX:+UseConcMarkSweepGC"
                       "-Xmx256m"
                       "-XX:+HeapDumpOnOutOfMemoryError"
                       #_"-XX:+PrintCompilation"
                       #_"-XX:+UnlockDiagnosticVMOptions"
                       #_"-XX:+PrintInlining"]
  :global-vars {*warn-on-reflection* true})
