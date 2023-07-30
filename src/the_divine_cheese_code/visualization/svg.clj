(ns the-divine-cheese-code.visualization.svg)

(defn comparator-over-maps
  [comparison-fn ks]
  (fn [maps]
    (println comparison-fn)
    (println ks)
    (println maps)
    (zipmap ks
            (map (fn [k] (apply comparison-fn (map k maps)))
                 ks))))

(def custom-min (comparator-over-maps clojure.core/min [:lat :lng]))
(def custom-max (comparator-over-maps clojure.core/max [:lat :lng]))

(custom-min [{:lat 1 :lng 3} {:lat 5 :lng 0}])
(custom-max [{:lat 1 :lng 3} {:lat 5 :lng 0}])

(defn translate-to-00
  [locations]
  (let [mincoords (custom-min locations)]
    (map #(merge-with - % mincoords) locations)))

(defn scale
  [width height locations]
  (let [maxcoords (custom-max locations)
        ratio {:lat (/ height (:lat maxcoords))
               :lng (/ width (:lng maxcoords))}]
    (map #(merge-with * % ratio) locations)))

(defn latlng->point
  "Convert lat/lnt map to comma-separated string"
  [latlng]
  (str (:lng latlng) "," (:lat latlng)))

(defn points
  [locations]
  (clojure.string/join " " (map latlng->point locations)))

(defn line
  [points]
  (str "<polyline points=\"" points "\" />"))

(defn transform
  "Just chains other functions"
  [width height locations]
  (->> locations
       translate-to-00
       (scale width height)))

(defn xml
  "svg 'template', which also flips the coordinate system"
  [width height locations]
  (str "<svg height=\"" height "\" width=\"" width "\">"
       ;; these two g tags change the coordinate system so that
       ;; 0,0 is in the lower-left corner, instead of SVG's default
       ;; upper-left corner
       "<g transform=\"translate(0," height ")\">"
       "<g transform=\"rotate(-90)\">"
       (-> (transform width height locations)
           points
           line)
       "</g></g>"
       "</svg>"))

(println "I was required!")
