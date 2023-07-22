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
; so we load the other namespace (otherwise, the current would not even know it)
(require 'the-divine-cheese-code.visualization.svg)

; refer, to join map of the current + the new namespace
(refer 'the-divine-cheese-code.visualization.svg)

; after refer
(ns-interns 'the-divine-cheese-code.core)
(ns-map 'the-divine-cheese-code.core)
(get (ns-map 'the-divine-cheese-code.core) 'points)

(points heists)
