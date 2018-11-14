;; making this an adzerk artifact becauae cljsjs doesn't currently have this version of papaparse
(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.10.3" :scope "test"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "4.6.1")
(def +version+ (str +lib-version+ "-1"))

(def download-url
  (str "https://github.com/mholt/PapaParse/archive/" +lib-version+ ".tar.gz"))

(task-options!
  pom  {:project     'adzerk/papaparse
        :version     +version+
        :description "Fast and powerful CSV (delimited text) parser that gracefully handles large files and malformed input"
        :url         "http://PapaParse.com"
        :license     {"MIT" "http://opensource.org/licenses/MIT"}
        :scm         {:url "https://github.com/cljsjs/packages"}})

(deftask package []
  (comp
   (download :url download-url
             :checksum "ce12b81444f25e4179aacdaba7826396"
             :decompress true
             :compression-format "gz"
             :archive-format "tar")
   (sift :move {#"^PapaParse-.*/papaparse.js" "cljsjs/papaparse/development/papaparse.inc.js"
                #"^PapaParse-.*/papaparse.min.js" "cljsjs/papaparse/production/papaparse.min.inc.js"})
   (sift :include #{#"^cljsjs"})
   (deps-cljs :name "adzerk.papaparse")
   (pom)
   (jar)))
