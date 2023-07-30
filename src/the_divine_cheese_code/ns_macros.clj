(ns the-divine-cheese-code.ns-macros)

; *ns* or 'the-divine-cheese-code.core
(ns-interns 'the-divine-cheese-code.core)
(ns-map 'the-divine-cheese-code.core)
(get (ns-map 'the-divine-cheese-code.core) 'points)

; require
; so we load the other namespace
; (otherwise, the current would not even know it exists)
(the-divine-cheese-code.visualization.svg/points {})        ; -> error class not found
(require 'the-divine-cheese-code.visualization.svg)
(the-divine-cheese-code.visualization.svg/points {})        ; -> now it works
; and alias
(alias 'svg 'the-divine-cheese-code.visualization.svg)
(svg/points {})                                             ; -> now it works with alias
; or all together
(require '[the-divine-cheese-code.visualization.svg :as svg])

; refer, to join map of the current + the new namespace
(refer 'the-divine-cheese-code.visualization.svg)

; use = require + refer
(use 'the-divine-cheese-code.visualization.svg)
; with alias
(use '[the-divine-cheese-code.visualization.svg :as svg])
(= svg/points points)

; after refer
(ns-interns 'the-divine-cheese-code.core)
(ns-map 'the-divine-cheese-code.core)
(get (ns-map 'the-divine-cheese-code.core) 'points)

(points heists)
(svg/points heists)

; book example about use + alias
; looks like it is an error -> https://clojureverse.org/t/clojure-for-the-brave-and-true-error-in-chapter-6/4029/7
; the book online has the error too -> https://www.braveclojure.com/organization/
(use '[the-divine-cheese-code.visualization.svg :as xpto :only [points]])
(refer 'the-divine-cheese-code.visualization.svg :as :only ['points])
(xpto/latlng->point {})
(xpto/points {})
(points {})
(latlng->point {})
; end example that doesn't work

; Understanding the `ns` macro

; ns refers core automatically
(ns the-divine-cheese-code.ns-macros)
; =
(in-ns 'the-divine-cheese-code.ns-macros)
(clojure.core/refer 'clojure.core)

; we can use :exclude by using :refer-clojure
; we have :refer-clojure :require :use :import :load and :gen-class
(ns the-divine-cheese-code.ns-macros
  (:refer-clojure :exclude [println]))
; =
(in-ns 'the-divine-cheese-code.ns-macros)
(clojure.core/refer 'clojure.core :exclude ['println])

; require
(ns the-divine-cheese-code.ns-macros
  (:require the-divine-cheese-code.visualization.svg))
; =
(in-ns 'the-divine-cheese-code.ns-macros)
(clojure.core/refer 'clojure.core)
(require 'the-divine-cheese-code.visualization.svg)

; require + alias
(ns the-divine-cheese-code.ns-macros
  (:require [the-divine-cheese-code.visualization.svg :as svg]))
; =
(in-ns 'the-divine-cheese-code.ns-macros)
(clojure.core/refer 'clojure.core)
(require ['the-divine-cheese-code.visualization.svg :as 'svg])

; require + refer names
(ns the-divine-cheese-code.ns-macros
  (:require [the-divine-cheese-code.visualization.svg :refer [points]]))
; =
(in-ns 'the-divine-cheese-code.ns-macros)
(clojure.core/refer 'clojure.core)
(require 'the-divine-cheese-code.visualization.svg)
(refer 'the-divine-cheese-code.visualization.svg :only ['points])

; require + refer all
(ns the-divine-cheese-code.ns-macros
  (:require [the-divine-cheese-code.visualization.svg :refer :all]))
; =
(in-ns 'the-divine-cheese-code.ns-macros)
(clojure.core/refer 'clojure.core)
(require 'the-divine-cheese-code.visualization.svg)
(refer 'the-divine-cheese-code.visualization.svg)

; finally, use
(ns the-divine-cheese-code.ns-macros
  (:use [clojure.java browse io]))
; =
(in-ns 'the-divine-cheese-code.ns-macros)
(clojure.core/refer 'clojure.core)
(use 'clojure.java.browse)
(use 'clojure.java.io)
