(defproject clj-fo "0.0.1"
  :description "Clojure PDF library & wrapper for Apache FOP"
  :url "https://github.com/fmw/cljfo"
  :license {:name "Apache License, version 2."
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.xml "0.0.3"]
                 [org.apache.pdfbox/pdfbox "1.6.0"]
                 [org.apache.xmlgraphics/fop "1.0"]]
  :source-paths ["src"]
  :test-paths ["test"])
