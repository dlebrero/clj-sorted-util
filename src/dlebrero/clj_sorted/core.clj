(ns dlebrero.clj-sorted.core
  (:use clojure.test))

(defn- concat-two-sorted-seqs
  [^java.util.Comparator comp coll1 coll2]
  (let [f-coll1 (first coll1)
        f-coll2 (first coll2)]
    (if (and f-coll1 f-coll2)
      (let [c (.compare comp f-coll1 f-coll2)]
        (cond
         (< 0 c) (lazy-seq (cons f-coll2 (concat-two-sorted-seqs comp (rest coll2) coll1)))
         (> 0 c) (lazy-seq (cons f-coll1 (concat-two-sorted-seqs comp (rest coll1) coll2)))
         :else (lazy-seq (cons f-coll1 (concat-two-sorted-seqs comp (rest coll1) (rest coll2))))))
      (if f-coll1
        coll1
        coll2))))

(defn concat-sorted
  "Returns a sorted lazy sequence of sorted collections. Note that the collections must be already sorted"
  ([colls]
     (concat-sorted compare colls))
  ([comp colls]
     (if (seq colls)
       (reduce #(concat-two-sorted-seqs comp %1 %2) colls)
       [])))

(defn concat-sorted-by
  "Given a seq of colls sorted-by keyfn, returns a lazy seq concatenation of those colls keeping the sorted-by order."
  ([keyfn colls]
     (concat-sorted-by keyfn compare colls))
  ([keyfn ^java.util.Comparator comp colls]
     (concat-sorted (fn [x y] (. comp (compare (keyfn x) (keyfn y)))) colls)))

(deftest concat-one-element-collections
  (is (= (concat-sorted [[4] [2]]) [2 4]))
  (is (= (concat-sorted [[2] [4]]) [2 4])))

(deftest concat-two-element-collections
  (is (= (concat-sorted [[3 5] [2 4]]) [2 3 4 5]))
  (is (= (concat-sorted [[2 4] [3 5]]) [2 3 4 5])))

(deftest concat-uneven-number-of-elements
  (is (= (concat-sorted [[1 3 5] [4]]) [1 3 4 5])))

(deftest concat-with-duplicates
  (is (= (concat-sorted [[1 3] [1 3]]) [1 3]))
  (is (= (concat-sorted [[1 3 4] [1 3]]) [1 3 4]))
  (is (= (concat-sorted [[1 3] [1 3 4]]) [1 3 4])))

(deftest other-than-2-colls
  (is (= (concat-sorted [[1 4] [2 3] [0 9]]) [0 1 2 3 4 9]))
  (is (= (concat-sorted [[1 4]]) [1 4]))
  (is (= (concat-sorted []) [])))

(deftest other-order
  (is (= (concat-sorted > [[5 3 1] [4 0]]) [5 4 3 1 0])))

(deftest concat-sorted-by-collections
  (is (= (concat-sorted-by :age [[{:age 2 :who :joe} {:age 20 :who :tom}]
                                 [{:age 1 :who :dan} {:age 22 :who :foo}]])
         [{:age 1 :who :dan} {:age 2 :who :joe} {:age 20 :who :tom} {:age 22 :who :foo}])))

(defonce huge1 (sort (take 40000 (repeatedly #(rand-int 1000000000)))))
(defonce huge2 (sort (take 40000 (repeatedly #(rand-int 1000000000)))))
(defonce huge3 (sort (take 40000 (repeatedly #(rand-int 1000000000)))))
(run-tests)
