(ns the-divine-cheese-code.core)

(def heists [{:location    "Cologne, Germany"
              :cheese-name "Archbishop Hildebold's Cheese Pretzel"
              :lng         50.95
              :lat         6.97}
             {:location    "Zurich, Switzerland"
              :cheese-name "The Standard Emmental"
              :lng         47.30
              :lat         8.55}
             {:location    "Marseille, France"
              :cheese-name "Fromage"
              :lng         43.40
              :lat         5.37}
             {:location    "Zurich, Switzerland"
              :cheese-name "The Lesser Emmental"
              :lng         47.37
              :lat         8.55}
             {:location    "Vatican City"
              :cheese-name "The Cheese of Turin"
              :lng         41.90
              :lat         12.45}])

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

