(ns profile.fannkuchen
  (use [clojure.contrib.combinatorics :only [lex-permutations]])
  (:gen-class))

(set! *warn-on-reflection* true)

(defmacro copy-array [from to cnt]
  `(loop [i#  ~cnt]
     (if (zero? i#)
       (aset ~to i# (aget ~from i#))
       (do 
	 (aset ~to i# (aget ~from i#))
	 (recur (unchecked-dec-long i#))))))

(defmacro swap-array [a p1 p2]
  `(let [t# (aget ~a ~p1)]
     (aset ~a ~p1 (aget ~a ~p2))
     (aset ~a ~p2 t#)
     ~a))

(defmacro reverse-first-n [a n]
  `(let [n2# (unchecked-add-long (unchecked-divide-long ~n 2) (unchecked-remainder-long ~n 2))]
     (loop [i# 0]
       (if (< i# n2#)
	 (do
	   (swap-array ~a i# (unchecked-subtract-long ~n i#))
	   (recur (unchecked-inc-long i#)))
	 ~a))))

(defmacro fannkuchen-flip [m]
  `(loop [m# ~m c# 0]
     (let [f# (aget m# (long 0))]
       (if (zero? f#)
	 c#
	 (recur (reverse-first-n m# f#) (unchecked-inc-long c#))))))

(defmacro next-permutation [perm r size cnt]
  `(loop [r# ~r]
     (if (= r# ~size)
       r#
       (let [p0# (aget ~perm 0)
	     ncnt# (unchecked-dec-long (aget ~cnt r#))]
	 (loop [i# 0]
	   (if (< i# r#)
	     (do
	       (aset ~perm i# (aget ~perm (unchecked-inc-long i#)))
	       (recur (unchecked-inc-long i#)))))
	 (aset ~perm r# p0#)
	 (aset ~cnt r# ncnt#)
	 (if (zero? ncnt#)
	   (recur (unchecked-inc-long r#))
	   r#)))))


(defmacro max' [x y]
  `(if (> ~x ~y) ~x ~y))

(defn fannkuchen' [size]
  (let [size (long size)
	perm1 (long-array (range size))
	perm (long-array size)
        cnt (long-array size)
        lst (unchecked-dec-long size)]
    (loop [r size max-flips 0 nperms 0 checksum 0]
      (loop [r r]
	(if-not (= r 1)
	  (do
	    (aset cnt (unchecked-dec-long r) r)
	    (recur (unchecked-dec-long r)))))
      (let [r (next-permutation perm1 1 size cnt)
	    _ (copy-array perm1 perm lst)
	    flips (fannkuchen-flip perm)]
	(if (= r size)
	  (do
	    (println (or (and (zero? (bit-and nperms 1)) (unchecked-subtract-long checksum flips)) (unchecked-add-long checksum flips)))
	    (max' flips max-flips))
	  (recur (long r) (long (max' flips max-flips)) (unchecked-inc-long nperms) (long (or (and (zero? (bit-and nperms 1)) (unchecked-subtract-long checksum flips)) (unchecked-add-long checksum flips)))))))))

#_(defn permutations' [size]
  (let [size (long size)
	perm1 (long-array (range size))
        cnt (long-array size)
        lst (unchecked-dec-long size)]
    (loop [r size ps []]
      (loop [r (long r)]
	(if-not (= r 1)
	  (do
	    (aset cnt (unchecked-dec-long r) r)
	    (recur (unchecked-dec-long r)))))
      (let [r (next-permutation perm1 1 size cnt)]
	(if (= r size)
	  (cons (doall (vec perm1)) ps)
	  (recur (long r) (cons (doall (vec perm1)) ps)))))))


(defn -main [a]
  (let [n (Integer/parseInt a)]
    (println 
     (str "Pfannkuchen(" n ") =") 
     (fannkuchen' n))))