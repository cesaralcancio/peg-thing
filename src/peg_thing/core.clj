(ns peg-thing.core
  (:require [clojure.string :as s])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def ds {1 {:pegged true :connections {6 3 4 2}}
         2 {:pegged true :connections {9 5 7 4}}
         3 {:pegged true :connections {10 6 8 5}}
         4 {:pegged true :connections {13 8 11 7 6 5 1 2}}
         5 {:pegged true :connections {14 9 12 8}}
         6 {:pegged true :connections {15 10 13 9 4 5 1 3}}})
;; position 1 can jump to 6 over 3
;; position 1 can jump to 4 over 2
(println ds)

(defn tri*
  "Generates lazy sequence of triangular numbers"
  ([] (tri* 0 1))
  ([sum n]
   (let [new-sum (+ sum n)]
     (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

(def tri (tri*))
(take 5 tri)

(defn triangular?
  "Is the number triangular? e.g. 1, 3, 6, 10, 15, etc."
  [n]
  (= n (last (take-while #(>= n %) tri))))
(triangular? 5)
(triangular? 6)
(triangular? 200)
(triangular? 210)
(triangular? 210000)

(defn row-tri
  "The triangular number at the end of the row n"
  [n]
  (last (take n tri)))
(row-tri 4)

(defn row-num
  "Returns row number the position belongs to: pos 1 in row 1,
  positions 2 and 3 in row 2, etc."
  [pos]
  (inc (count (take-while #(> pos %) tri))))
; (take-while #(> 10 %) tri)
(row-num 11)
(row-num 5)

(defn connect
  "Form a mutual connection between two positions"
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))
(connect {} 15 1 2 4)
(connect {} 15 7 8 9)
(connect {} 15 5 9 14)

(defn connect-right
  [board max-pos pos]
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    (if-not (or (triangular? neighbor)
                (triangular? pos))
      (connect board max-pos pos neighbor destination)
      board)))

(defn connect-down-left
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ row pos)
        destination (+ 1 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ 1 row pos)
        destination (+ 2 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(connect-down-left {} 15 1)
(connect-down-left {} 15 3)
(connect-down-right {} 15 3)

(defn add-pos
  "Pegs the position and performs connections"
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce (fn [new-board connection-creation-fn]
              (connection-creation-fn new-board max-pos pos))
            pegged-board
            [connect-right connect-down-left connect-down-right])))
(add-pos {} 15 1)

(defn clean
  [text]
  (reduce
    (fn [t t-fn]
      (t-fn t))
    text
    [s/trim #(s/replace % #"lol" "LOL")]))

(defn new-board
  "Creates a new board with the given number of rows"
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce (fn [board pos] (add-pos board max-pos pos))
            initial-board
            (range 1 (inc max-pos)))))
(def board-1 (new-board 5))
(println board-1)

(defn pegged?
  "Does the position have a peg in it?"
  [board pos]
  (get-in board [pos :pegged]))
(pegged? board-1 7)

(defn remove-peg
  [board pos]
  (assoc-in board [pos :pegged] false))
(remove-peg board-1 7)

(defn place-peg
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))

(defn valid-moves
  "Return a map of all valid moves for pos, where the key is the
  destination and the value is the jumped position"
  [board pos]
  (into {}
        (filter (fn [[destination jumped]]
                  (and (not (pegged? board destination))
                       (pegged? board jumped)))
                (get-in board [pos :connections]))))
(valid-moves board-1 2)
(valid-moves ds 2)

(def my-board (assoc-in (new-board 5) [4 :pegged] false))
(valid-moves my-board 1)
(valid-moves my-board 6)
(valid-moves my-board 11)
(valid-moves my-board 13)
(valid-moves my-board 4)

(defn valid-move?
  "Return jumped position if the move from p1 to p2 is valid,
  nil otherwise"
  [board p1 p2]
  (get (valid-moves board p1) p2))
(valid-move? my-board 8 4)
(valid-move? my-board 1 4)

(defn make-move
  "Move peg from p1 to p2, removing jumped peg"
  [board p1 p2]
  (if-let [jumped (valid-move? board p1 p2)]
    (move-peg (remove-peg board jumped) p1 p2)))
(make-move my-board 1 4)

(defn can-move?
  "Do any of the pegged positions have valid moves?"
  [board]
  (some (comp not-empty (partial valid-moves board))
        (map first (filter #(get (second %) :pegged) board))))
; testing to understand the function
(do my-board)
(filter #(:pegged (second %)) my-board)
(map first (filter #(:pegged (second %)) my-board))
(some (comp not-empty (partial valid-moves my-board))
      '(7 1 15 13 6 3 12 2 11 9 5 14 10 8))
(some #(->> %
            (valid-moves my-board)
            not-empty)
      '(7 1 15 13 6 3 12 2 11 9 5 14 10 8))
(valid-moves my-board 1)
(valid-move? my-board 1 4)
(can-move? my-board)
(can-move? board-1)

(def alpha-start 97)
(def alpha-end 123)
(def letters (map (comp str char) (range alpha-start alpha-end)))
(def pos-chars 3)

(defn render-pos
  [board pos]
  (str (nth letters (dec pos))
       (if (get-in board [pos :pegged])
         "0" "-")))

(defn row-positions
  "Return all positions in the given row"
  [row-num]
  (range (inc (or (row-tri (dec row-num)) 0))
         (inc (row-tri row-num))))

(defn row-padding
  "String off spaces to add to the beginning of a row to center if"
  [row-num rows]
  (let [pad-length (/ (* (- rows row-num) pos-chars) 2)]
    (apply str (take pad-length (repeat " ")))))

(defn render-row
  [board row-num]
  (str (row-padding row-num (:rows board))
       (s/join " " (map (partial render-pos board)
                        (row-positions row-num)))))

(defn print-board
  [board]
  (doseq [row-num (range 1 (inc (:rows board)))]
    (println (render-row board row-num))))

(print-board my-board)

(defn letter->pos
  "Converts a letter string to the corresponding position number"
  [letter]
  (inc (- (int (first letter)) alpha-start)))

(defn get-input
  "Waits for user to enter text and hit enter,
  then cleans the input"
  ([] (get-input nil))
  ([default]
   (let [input (s/trim (read-line))]
     (if (empty? input)
       default
       (s/lower-case input)))))

(defn characters-as-strings
  "Given a string, return a collection consisting of each indivisual character"
  [string]
  (re-seq #"[a-zA-Z]" string))
(characters-as-strings "a b")
(characters-as-strings "a    cb")

(defn user-entered-invalid-move
  "Handles the next step after a user has entered an invalid move"
  [board]
  (println "\n!!! That was an invalid move :(\n")
  (prompt-move board))

(defn prompt-empty-peg [board]
  (println "Here's your board:")
  (print-board board)
  (println "Remove which peg? [e]")
  (prompt-move (remove-peg board (letter->pos (get-input "e")))))

(defn prompt-rows []
  (println "How many rows? [5]")
  (let [rows (Integer. (get-input 5))
        board (new-board rows)]
    (prompt-empty-peg board)))

(defn game-over
  "Announce the game is over and prompt to play again"
  [board]
  (let [remaining-pegs (count (filter :pegged (vals board)))]
    (println "Game over! You had" remaining-pegs "pegs left:")
    (print-board board)
    (println "Play again? y/n [y]")
    (let [input (get-input "y")]
      (if (= "y" input)
        (prompt-rows)
        (do
          (println "Bye!")
          (System/exit 0))))))

(defn user-entered-valid-move
  "Handles the next step after a user has entered a valid move"
  [board]
  (if (can-move? board)
    (prompt-move board)
    (game-over board)))

(defn prompt-move
  [board]
  (println "\nHere's your board:")
  (print-board board)
  (println "Move from where to where? Enter two letters:")
  (let [input (map letter->pos (characters-as-strings (get-input)))]
    (if-let [new-board (make-move board (first input) (second input))]
      (user-entered-valid-move new-board)
      (user-entered-invalid-move board))))

; (prompt-rows)

; book exercises
((comp #(+ 1 %) #(+ 1 %)) 1)
((partial #(+ 1 %1 %2) 1) 1)
(assoc-in {:a {:a1 {:a11  "a11"
                    :a112 "a112"}
               :a2 {:a221 "a221"}}
           :b {:b1 {:b11 "b11"}}}
          [:a :a2 :a22] "a22")

(assoc-in {:a {:a1 {:a11 "a11"}}
           :b {:b1 {:b11 "b11"}}}
          [:c :c2 :c22] "c22")

(defn my-assoc-in
  [m [k & ks] v]
  (let [reversed (reverse (conj ks k))                      ; add all keys and reverse, so we can reduce it from inside to outside
        temp {}                                             ; build up the new map
        [pqp _] (reduce (fn [[value position] key]          ; we need the position, so we can merge with existing values
                          (let [result (if (nil? position)
                                    (vector (assoc temp key value)
                                            (conj ks k))
                                    (let [dropped (drop-last position)
                                          current-value (get-in m dropped)
                                          merged-values (merge current-value value)]
                                      (vector (assoc temp key merged-values)
                                              dropped)))]
                            result))
                        [v nil]
                        reversed)]
    (merge m pqp)))

(my-assoc-in {:a {:a1 {:a11 "a11"}}
              :b {:b1 {:b11 "b11"}}}
             [:a :a2 :a22] "a22")

(my-assoc-in {:a {:a1 {:a11 "a11"}
                  :a2 {:a22x "a22x"}}
              :b {:b1 {:b11 "b11"}}}
             [:a :a2 :a22] "a22")

(assoc {:a "b"} :b "b")
