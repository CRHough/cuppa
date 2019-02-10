(ns cuppa.core
  (:require [cuppa.grounds :refer [code reify-ifn]])
  (:import [org.graalvm.polyglot Value])
  (:gen-class))



(set! *warn-on-reflection* true)



(def nmf (reify-ifn (.getMember ^Value (code) "main")))
(def npf (reify-ifn (.getMember ^Value (code) "printHello")))



(defn -main
  [& _args]
  (println "int function returned" (nmf))
  (npf))
