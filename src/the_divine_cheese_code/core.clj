(ns the-divine-cheese-code.core
  (:require [clojure.java.browse :as browse]
            [the-divine-cheese-code.visualization.svg :refer [xml]]))

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

(defn url
  [filename]
  (str "file:///"
       (System/getProperty "user.dir")
       "/"
       filename))

(defn template
  [contents]
  (str "<style>polyline"
       "{ fill:none; stroke:#5881d8; stroke-width:3}"
       "</style>"
       contents))

(defn -main
  [& args]
  (let [filename "map.html"]
    (->> heists
         (xml 50 100)
         template
         (spit filename))
    (browse/browse-url (url filename))))

(-main)