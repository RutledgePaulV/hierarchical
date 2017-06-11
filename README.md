# hierarchical

[![Build Status](https://travis-ci.org/RutledgePaulV/hierarchical.svg?branch=develop)](https://travis-ci.org/RutledgePaulV/hierarchical)

Hierarchical is a library providing views over associative data structures that utilize
Clojure's hierarchy semantics for membership tests rather than strict values checking.
  
## Usage

```clojure
(ns example
  (:require [hierarchical.core :as h]))

(derive ::pets ::animals)
(derive ::farm ::animals)
(derive ::cows ::farm)
(derive ::cats ::pets)
(derive ::dogs ::pets)

(def kingdom
    {::animals {:eat "Grass"} 
     ::pets {:eat "Pet Food"}
     ::cats {:eat "Fancy Feast"}})

(def hiera (h/hierarchical kingdom))

(::cats hiera)
; {:eat "Fancy Feast"}

(::dogs hiera)
; {:eat "Pet Food"}

(::cows hiera)
; {:eat "Grass"}

```

### License

This project is licensed under [MIT license](http://opensource.org/licenses/MIT).