(ns regexdna
  (import java.util.regex.Matcher)
  (:gen-class))

(set! *warn-on-reflection* true)

(def replacements
  ["" "(c|g|t)" "" "(a|g|t)" "" "" "" "(a|c|t)"
   "" "" "(g|t)" "" "(a|c)" "(a|c|g|t)" "" ""
   "" "(a|g)" "(c|g)" "" "" "(a|c|g)" "(a|t)" ""
   "(c|t)"])

(def regexps
  [#"agggtaaa|tttaccct"
  #"[cgt]gggtaaa|tttaccc[acg]"
  #"a[act]ggtaaa|tttacc[agt]t"
  #"ag[act]gtaaa|tttac[agt]ct"
  #"agg[act]taaa|ttta[agt]cct"
  #"aggg[acg]aaa|ttt[cgt]ccct"
  #"agggt[cgt]aa|tt[acg]accct"
  #"agggta[cgt]a|t[acg]taccct"
  #"agggtaa[cgt]|[acg]ttaccct"])

(defn replace-stuff [#^String data data-c]
  (let [buffer (StringBuffer. (int(* data-c 2)))
        matcher #^Matcher (re-matcher #"[BDHKMNRSVWY]" data)
        a (int \A)]
    (while (.find matcher)
      (.appendReplacement matcher buffer (get replacements (- (int (.charAt data (.start matcher))) a) )))
    (.appendTail matcher buffer)
   (.length buffer)))
   

(defn count-matches [re #^String data]
  (let [matcher #^Matcher (re-matcher re data)]
    (loop [c 0]
      (if (.find matcher)
        (recur (inc c))
        c))))
        
(defn -main [& args]
  (let
    [raw (slurp *in*)
     data (.replaceAll #^String raw
            ">.*\n|\n" "")
     data-c (count data) 
     rcount (future (replace-stuff data data-c))
     counts (map (fn [r] (future (count (re-seq r data)))) regexps)]
    (time (do
    (doseq [res (map (fn [r c] (vector (str r) @c)) regexps counts)]
      (apply println res))
    (println)
    (println (count raw))
    (println data-c)
    (println @rcount))))
  (shutdown-agents))
      