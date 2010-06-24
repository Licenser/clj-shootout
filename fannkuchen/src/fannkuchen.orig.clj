(ns fannkuchen
  (use [clojure.contrib.combinatorics :only [lex-permutations]])
  (:gen-class))

(set! *warn-on-reflection* true)


(defmacro swap-array [a p1 p2]
  `(let [t# (aget ~a ~p1)]
     (aset ~a ~p1 (aget ~a ~p2))
     (aset ~a ~p2 t#)
     ~a))

(defmacro reverse-first-n [a n]
  `(let [n# (dec ~n)]
     (loop [m# (int (dec (quot ~n 2)))]
      (if (zero? m#)
        (swap-array ~a m# (- n# m#))
        (do 
          (swap-array ~a m# (- n# m#))
          (recur (dec m#)))))))

(defmacro fannkuchen [m]
  `(loop [m# (int-array ~m) c# 0]
    (let [f# (int (aget m# 0))]
      (if (= f# 1)
        c#
        (recur (reverse-first-n m# f#) (inc c#))))))

(defn -main [a]
  (let [n (Integer/parseInt a)]
    (println 
      (str "Pfannkuchen(" n ") =") 
      (loop [ps (lex-permutations (range 1 (inc n))) m 0]
        (if (empty? ps)
          m
          (let [p (first ps)]
            (recur (rest ps) (long (max (fannkuchen p) m)))))))))