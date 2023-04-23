(defproject cellular-automata "0.1.0-SNAPSHOT"
  :description "Visualize elementary cellular automata in a terminal."
  :url "http://example.com/FIXME"
  :license {:name "GNU General Public License v3"
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot cellular-automata.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
