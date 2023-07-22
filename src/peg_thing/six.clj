(ns peg-thing.six)

(ns-name *ns*)
(println *ns*)
(do *ns*)

(quote (map inc [1 2 (+ 1 1)]))
'(map inc [1 2 (+ 1 1)])
`(map inc [1 2 (+ 1 1)])

(list 1 2 3)
'(1 2 3)

(def great-books ["East of Eden"
                  "The Glass Bead Game"])
; returns #'peg-thing.six/great-books
; this is the read former
(var great-books)
(var-get #'peg-thing.six/great-books)
(eval #'peg-thing.six/great-books)
(do great-books)

(ns-interns *ns*)
(get (ns-interns *ns*) 'great-books)

(ns-interns *ns*)
(ns-map *ns*)
