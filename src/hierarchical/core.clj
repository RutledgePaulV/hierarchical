(ns hierarchical.core
  (:import (clojure.lang PersistentHashMap IPersistentMap Associative IFn IKVReduce ILookup IMapIterable IPersistentCollection Seqable APersistentMap)
           (hierarchical.core HierarchicalMap)))


(defn find-hierarchically [h m k]
  (let [ks (concat [k] (ancestors h k))]
    (loop [k ks]
      (when k
        (let [entry (.entryAt m k)]
          (if entry
            entry
            (recur (next ks))))))))

(deftype HierarchicalMap
  [h ^APersistentMap delegate]

  Associative
  (containsKey [this k]
    (some? (find-hierarchically h delegate k)))

  (entryAt [this k]
    (find-hierarchically h delegate k))

  (assoc [this k v]
    (HierarchicalMap. h
      (assoc delegate k v)))

  IFn
  (invoke [this k]
    (.valAt this k))

  (invoke [this k not-found]
    (.valAt this k not-found))

  ILookup
  (valAt [this k]
    (when-let [entry (find-hierarchically h delegate k)]
      (val entry)))

  (valAt [this k not-found]
    (if-let [entry (find-hierarchically h delegate k)]
      (val entry)
      not-found))

  IPersistentCollection
  (count [this]
    (.count delegate))

  (cons [this v]
    (HierarchicalMap. h
      (.cons delegate v)))

  (empty [this]
    (HierarchicalMap. h
      (.empty delegate)))

  IPersistentMap
  (assoc [this k v]
    (HierarchicalMap. h
      (assoc delegate k v)))

  (assocEx [this k v]
    (HierarchicalMap. h
      (.assocEx delegate k v)))

  (without [this k]
    (HierarchicalMap. h
      (.without delegate k)))

  Seqable
  (seq [this]
    (.seq delegate))

  Iterable
  (iterator [this]
    (.iterator delegate))

  Object
  (toString [this]
    (.toString delegate)))

(defn hierarchical
  ([assocative] (hierarchical global-hierarchy assocative))
  ([h assocative]
   (cond
     (map? assocative)
     (HierarchicalMap. h assocative)
     :else
     (throw
       (UnsupportedOperationException.
         "I don't support the provided type at this time.")))))