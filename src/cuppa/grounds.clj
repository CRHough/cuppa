(ns cuppa.grounds
  (:import [org.graalvm.polyglot Context Source Value]
           [clojure.lang IFn]
           [java.io File])
  (:gen-class))



(set! *warn-on-reflection* true)



(defn code
  []
  ; Each of these, according to the docs, should return cached values rather than re-evaluate.
  ; Something strange is going on though, they can't be top-level defs (except s).
  ; Doing so, for c at least, throws errors about nativedestructor stored in pre-initialized context, and native object
  ; (LibFFISymbol) stored in pre-initialized context, and a few other similar things.
  (let [c (-> (Context/newBuilder (into-array ["llvm"]))
              (.allowAllAccess true)
              (.build))
        s (-> (Source/newBuilder "llvm" (File. "./hello.bc"))
              (.build))
        v (-> c (.eval s))]
    v))



; ----------------------------------------------------------------------------------------------------------------------
; These were taken from
; [GraalVM Polyglot with Clojure and JavaScript](https://blog.taylorwood.io/2018/11/26/graal-polyglot.html)

(defn execute [^Value execable & args]
  (.execute execable (object-array args)))

(declare value->clj)

(defmacro reify-ifn
  "Convenience macro for reifying IFn for executable polyglot Values."
  [v]
  (let [invoke-arity (fn [n]
                       (let [args (map #(symbol (str "arg" (inc %))) (range n))]
                         (if (seq args)
                           `(~'invoke [this# ~@args] (value->clj (execute ~v ~@args)))
                           `(~'invoke [this#] (value->clj (execute ~v))))))]
    `(reify IFn
       ~@(map invoke-arity (range 22))
       (~'applyTo [this# args#] (value->clj (apply execute ~v args#))))))

(defn value->clj
  "Returns a Clojure (or Java) value for given polyglot Value if possible, otherwise throws."
  [^Value v]
  (cond
    (.isNull v) nil
    (.isHostObject v) (.asHostObject v)
    (.isBoolean v) (.asBoolean v)
    (.isString v) (.asString v)
    (.isNumber v) (.as v Number)
    (.canExecute v) (reify-ifn v)
    (.hasArrayElements v) (into [] (for [i (range (.getArraySize v))]
                                     (value->clj (.getArrayElement v i))))
    (.hasMembers v) (into {} (for [k (.getMemberKeys v)]
                               [k (value->clj (.getMember v k))]))
    :else (throw (Exception. "Unsupported value"))))
; ----------------------------------------------------------------------------------------------------------------------
