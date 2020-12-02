(ns vrepl.core
  (:require [cider.nrepl :refer [cider-nrepl-handler]]
            [nrepl.server :as nrepl]
            [ns-tracker.core :as tracker]
            [portal.api :as p]))

; https://cmdrdats.wordpress.com/2012/08/14/auto-reloading-code-with-clojure/

(defn- check-namespace-changes [track]
  (try
    (doseq [ns-sym (track)]
      (require ns-sym :reload))
    (catch Throwable e (.printStackTrace e)))
  (Thread/sleep 500))

(defn- start-nstracker []
  (let [track (tracker/ns-tracker ["src" "test" "resources"])]
    (doto
     (Thread.
      #(while true
         (check-namespace-changes track)))
      (.setDaemon true)
      (.start))))

(def p nil)

(defn- start-portal []
  (alter-var-root #'p (fn [_] (p/open)))
  (p/tap)
  (.addShutdownHook (Runtime/getRuntime) (Thread. #(p/close))))

(defn- start-nrepl []
  (:port (nrepl/start-server :handler cider-nrepl-handler)))

(defn- sh [args]
  (let [ps (.. (ProcessBuilder. args) inheritIO start)]
    (.addShutdownHook (Runtime/getRuntime) (Thread. #(.destroy ps)))
    ps))

(defn- start-vim [port args]
  (let [ex ["+set ft=clojure"
            (str "+Connect localhost:" port " .")
            "+insert"]]
    (sh (concat ["vim"] ex args))))

(defn -main [& args]
  (start-nstracker)
  (start-portal)
  (-> (start-nrepl) (start-vim args) .waitFor System/exit))
