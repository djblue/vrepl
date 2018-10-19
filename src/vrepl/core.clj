(ns vrepl.core
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [clojure.tools.nrepl.server :as nrepl]))

(defn sh [& args]
  (let [ps (.. (ProcessBuilder. args) inheritIO start)]
    (.addShutdownHook (Runtime/getRuntime) (Thread. #(.destroy ps)))
    ps))

(defn remote-send! [vi msg]
  (.waitFor (apply sh (concat vi ["--remote-send" msg]))))

(defn has-bin? [bin]
  (->> (s/split (System/getenv "PATH") #":")
       (map io/file)
       (mapcat file-seq)
       (some #(and (= (.getName %) bin) (.canExecute %)))))

(defn -main [& args]
  (let [id (name (gensym))
        vi (if (has-bin? "mvim")
             ["mvim" "-f" "--servername" id]
             ["vim" "--servername" id])
        ps (apply sh (concat vi args))
        port (:port (nrepl/start-server))]
    (Thread/sleep 100)
    (remote-send! vi ":set ft=clojure<CR>")
    (remote-send! vi (str ":Connect nrepl://localhost:" port "<CR><CR>"))
    (System/exit (.waitFor ps))))
