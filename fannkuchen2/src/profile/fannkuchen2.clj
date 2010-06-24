(ns profile.fannkuchen2
  (use [clojure.contrib.combinatorics :only [lex-permutations]])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn fankuchen [m]
  (loop [[f & _ :as m] m c 0]
    (if (= f 1 )
      c
      (recur 
        (concat 
          (reverse (take f m))
          (drop f m))
        (inc c)))))

(defn -main [a]
  (let [n (Integer/parseInt a)]
;;     (println "waiting")
   ;;  (read-line)
    (time
    (println 
      (str "Pfannkuchen(" n ") =") 
      (apply max (map fankuchen (lex-permutations (range 1 (inc n)))))))))
