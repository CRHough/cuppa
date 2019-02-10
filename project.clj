(defproject cuppa "0.1.0-alpha1"
  :description "Clojure calling C functions via GraalVM."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :profiles {:user {:plugins [[io.taylorwood/lein-native-image "0.3.0"]]
                    :native-image {;:graal-bin " FIXME - THIS WILL NOT WORK"
                                   :name "cuppa"
                                   :opts ["--no-server"
                                          "--report-unsupported-elements-at-runtime"
                                          "-Dclojure.compiler.direct-linking=true"
                                          ;"--verbose"
                                          ;"--enable-url-protocols=http,https"

                                          ; Native images need the used languages specified on the command line.
                                          ; Any languages used here need to have their graalvm/jre/language/* directories on the path.
                                          "--language:llvm"]}}}
  :main cuppa.core)
