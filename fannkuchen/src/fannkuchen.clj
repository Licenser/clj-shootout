(ns fannkuchen
  (use [clojure.contrib.combinatorics :only [lex-permutations]])
  (:gen-class))

(set! *warn-on-reflection* true)

(defmacro copy-array [from to cnt]
  `(loop [i# ~cnt]
     (if (zero? i#)
       (aset ~to i# (aget ~from i#))
       (do 
	 (aset ~to i# (aget ~from i#))
	 (recur (dec i#))))))

(defmacro swap-array [a p1 p2]
  `(let [t# (aget ~a ~p1)]
     (aset ~a ~p1 (aget ~a ~p2))
     (aset ~a ~p2 t#)
     ~a))

(defmacro reverse-first-n [a n]
  `(loop [m# (max 0 (long (dec (quot ~n 2))))]
     (if (zero? m#)
       (swap-array ~a m# (- ~n m#))
       (do 
         (swap-array ~a m# (- ~n m#))
         (recur (dec m#))))))

(defmacro fannkuchen-flip [m]
  `(loop [m# ~m c# 0]
     (let [f# (long (aget m# 0))]
       (if (zero? f#)
	 c#
	 (recur (reverse-first-n m# f#) (inc c#))))))

(defmacro next-permutation [perm r size cnt]
  `(loop [r# ~r]
     (if (= r# ~size)
       r#
       (let [p0# (long (aget ~perm 0))
	     ncnt# (dec (aget ~cnt r#))]
	 (loop [i# 0]
	   (if (< i# r#)
	     (do
	       (aset ~perm i# (long (aget ~perm (inc i#))))
	       (recur (inc i#)))))
	 (aset ~perm r# p0#)
	 (aset ~cnt r# ncnt#)
	 (if (zero? ncnt#)
	   (recur (inc r#))
	   r#)))))

(defn ^:static fannkuchen [#^long size]
  (let [perm1 (long-array (range size))
        perm (long-array size)
        cnt (long-array size)
        lst (dec size)]
    (loop [r size nperms 0 m 0 checksum 0]
      (loop [r (long r)]
        (if-not (= r 1)
	  (do
	    (aset cnt (dec r) r)
	    (recur (dec r)))))
      (copy-array perm1 perm lst)
      (let [flips (fannkuchen-flip perm)
	    r (next-permutation perm1 1 size cnt)]
	(if (= r size)
	  (do
	    (println checksum)
	    m)
	  (recur (long r) (inc nperms) (long (max m flips)) (long (if (even? nperms) (+ checksum flips) (- checksum flips)))))))))


(defn -main [a]
  (let [n (Integer/parseInt a)]
    (println 
     (str "Pfannkuchen(" n ") =") 
     (fannkuchen n))))