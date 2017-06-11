(ns hierarchical.core-test
  (:require [clojure.test :refer :all]
            [hierarchical.core :refer :all])
  (:import [hierarchical.core HierarchicalMap]))


(defn get-hierarchy []
  (->
    (make-hierarchy)
    (derive ::pets ::animals)
    (derive ::farm ::animals)
    (derive ::giraffe ::animals)
    (derive ::cows ::farm)
    (derive ::pigs ::farm)
    (derive ::cats ::pets)
    (derive ::dogs ::pets)))

(deftest hierarchical-map-lookups
  (testing "Exact entries used first, then hierarchy, then not-founds."
    (let [hierarchy (get-hierarchy)
          m {::dogs "Woof" ::pets "Love" ::farm "Corn"}
          hiera (hierarchical hierarchy m)]
      (is (= "Woof" (::dogs hiera)))
      (is (= "Love" (::cats hiera)))
      (is (= "Corn" (::pigs hiera)))
      (is (= "Corn" (::cows hiera "Some")))
      (is (= "Some" (::giraffe hiera "Some"))))))
