(ns clj-sorted-util.util
  (:import java.util.concurrent.PriorityBlockingQueue))

(defn- to-lazy-seq [^PriorityBlockingQueue queue-of-colls]
  (lazy-seq
     (let [coll-with-lowest (.poll queue-of-colls)
           next-items (next coll-with-lowest)]
       (when coll-with-lowest
         (if next-items
           (.offer queue-of-colls next-items))
         (cons (first coll-with-lowest) (to-lazy-seq queue-of-colls))))))

(defn remove-dups [coll]
  "Remove duplicates from a sorted collection. Note that nil elements are not supported"
  (if-let [f (first coll)]
    (lazy-seq (cons f (remove-dups (drop-while #(= f %) coll))))))

(defn compare-first-elements-with [comparator]
  "Returns a function that will compare the first elements of a collection with comparator"
  (fn [a b] (.compare comparator (first a) (first b))))

(defn concat-sorted
  "Returns a sorted lazy sequence of sorted collections. Note that the collections must be already sorted."
  ([colls]
     (concat-sorted compare colls))
  ([comp colls]
     (if (seq colls)
       (let [c (compare-first-elements-with comp)
             queue-of-colls (PriorityBlockingQueue. (count colls) c)]
         (dorun (map #(.offer queue-of-colls %) colls))
         (remove-dups (to-lazy-seq queue-of-colls)))
       [])))

(defn concat-sorted-by
  "Given a seq of colls sorted-by keyfn, returns a lazy seq concatenation of those colls keeping the sorted-by order."
  ([keyfn colls]
     (concat-sorted-by keyfn compare colls))
  ([keyfn ^java.util.Comparator comp colls]
     (concat-sorted (fn [x y] (. comp (compare (keyfn x) (keyfn y)))) colls)))
