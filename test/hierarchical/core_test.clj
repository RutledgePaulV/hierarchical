(ns hierarchical.core-test
  (:require [clojure.test :refer :all]
            [hierarchical.core :refer :all])
  (:import [hierarchical.core HierarchicalMap]))


(defn get-hierarchy []
  (->
    (make-hierarchy)
    (derive ::pets ::animals)
    (derive ::farm ::animals)
    (derive ::cows ::farm)
    (derive ::cats ::pets)
    (derive ::dogs ::pets)))


(deftest hierarchical-map-lookups
  (testing "An entry that exists directly in the map is always used."
    (let [hierarchy (get-hierarchy)
          m {::dogs "Woof" ::pets "Love"}
          hiera (hierarchical hierarchy m)]
      (is (= "Woof" (::dogs hiera))))))
