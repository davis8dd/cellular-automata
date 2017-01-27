;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Render functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; THIS HANDLES ALL SIDE-EFFECT FUNCTIONS

;; Separated rendering functions only for terminal... this should only handle strings
;; that are to be directly rendered to the terminal.  Higher-level business logic etc
;; should be in a separate namespace that interfaces model and view

(ns cellular-automata.output)
;; An example of displaying a grid.  This should probably be a unit test
;; This one looks neat!
;(display-grid (calculate-grid (create-rule 177) 50 [false false false false false false false false false false true false false false false false false false false false false]))

(defn display-grid
  "Display rendered grid of cellular automata"
  [rendered-grid]
  (doseq [rendered-grid-row (vec rendered-grid)] (println rendered-grid-row)))


(def render-chars {:cells {:on (char 9607)
;                              :off \ }
                              :off (char 9617)}
                   :border {:top (char 9552)
                               :bottom (char 9552)
                               :left (char 9553)
                               :right (char 9553)
                               :top-right (char 9559)
                               :bottom-right (char 9565)
                               :top-left (char 9556)
                               :bottom-left (char 9562)}})


;;(defn print-chars [chars-rendered] (doseq [a-char chars-rendered] (println (str "*** " a-char " ***"))))

;; Abstractly, display a grid.  This should eventually call an imported display library based on the output device used
(defn print-test-border [render-chars] (let [tl ((comp :top-left :border) render-chars)
                                                t ((comp :top :border) render-chars)
                                                tr ((comp :top-right :border) render-chars)
                                                l ((comp :left :border) render-chars)
                                                r ((comp :right :border) render-chars)
                                                bl (get-in render-chars [:border :bottom-left])
                                                b (get-in render-chars [:border :bottom])
                                                br (get-in render-chars [:border :bottom-right])
                                                on (get-in render-chars [:cells :on])
                                                off (get-in render-chars [:cells :off])]
                                          (println (str tl t t t tr))
                                          (println (str l off off off r))
                                          (println (str l off on off r))
                                          (println (str l off off off r))
                                          (println (str bl b b b br))))


(defn string-repeating-char [a-char times] (apply str (repeat times a-char)))
(defn add-border-top [render-view-chars border-width] (let [top-left-char (get-in render-view-chars [:border :top-left])
                                    top-right-char (get-in render-view-chars [:border :top-right])
                                    top-char (get-in render-view-chars [:border :top])]
                              (println "Border has width of " border-width) (apply str [top-left-char (string-repeating-char top-char border-width) top-right-char])))

(defn add-border-sides-to-row [render-view-chars render-row] (let [right-char (get-in render-view-chars [:border :right])
                                    left-char (get-in render-view-chars [:border :left])] (apply str (concat (str left-char) render-row (str right-char)))))

(defn add-border-sides [render-view-chars render-grid] (let [right-char (get-in render-view-chars [:border :right])
                                    left-char (get-in render-view-chars [:border :left])]
                              (reduce (fn [bordered-grid render-row] (conj bordered-grid (add-border-sides-to-row render-chars render-row))) []  render-grid)))
(defn add-border-bottom [render-view-chars border-width] (let [bottom-left-char (get-in render-view-chars [:border :bottom-left])
                                    bottom-right-char (get-in render-view-chars [:border :bottom-right])
                                    bottom-char (get-in render-view-chars [:border :bottom])]
                              (println "Width has value of " border-width) (apply str [bottom-left-char (string-repeating-char bottom-char border-width) bottom-right-char])))

;;(defn print-with-border [grid] (println "Grid has value of " grid) (add-border-sides grid) (add-border-top (count (first grid))) (add-border-bottom (count (first grid))))


;; SOMETHING LIKE THIS:
;; (->> a-grid (concat [["a" "b" "c"]]) (concat [["x" "y" "z"]])) 
;;(defn add-border [grid] (->> (add-border-sides grid) (concat [(add-border-top (count (first grid)))]) (concat [(add-border-bottom (count (first grid)))]) (map println) ))
(defn add-border [render-chars grid] (doseq [grid-row (vector (add-border-top render-chars (count (first grid))) (add-border-sides render-chars grid) (add-border-bottom render-chars (count (first grid))))] (println grid-row)))

(defn with-border [render-chars content]
    (let [content-width (count (first content))] (-> [(add-border-top render-chars content-width)]
                                                     (into (add-border-sides render-chars content))
                                                     (into [(add-border-bottom render-chars content-width)]))))

;; for each row in grid, add border-left to front and border-right to end of row
;; add new row to front of grid that's border top
;; add new row to back of grid that's border bottom

(defn render-row-to-str [render-row] (apply str (map #(get (get render-chars :cells) %) render-row)))
(defn row-to-render-row [row] (reduce (fn [render-row cell] (if cell (conj render-row :on) (conj render-row :off))) [] row))
;(defn rendered-grid [grid] (map render-row-to-str (map row-to-render-row grid)))
(defn rendered-grid [grid] (->> grid (map row-to-render-row) (map render-row-to-str)))
;; (map println (map render-row-to-str (map row-to-render-row (calculate-grid (create-rule 172) 20 [false true false false true true false true false]))) )

;;;;;;;;;;;;;;;;;;
;; full rendering:
; USE CASE 1: no border
(display-grid (rendered-grid (calculate-grid (create-rule 177) 45 [false false false false false false false false false false true false true false false false false false false false false])))
;fail;
; USE CASE 2: with border
(display-grid (with-border render-chars (rendered-grid (calculate-grid (create-rule 177) 45 [false false false false false false false false false false true false true false false false false false false false false]))))
;(display-grid (add-border render-chars (rendered-grid (calculate-grid (create-rule 177) 45 [false false false false false false false false false false true false true false false false false false false false false]))))
;
;;;;;;;;;;;;;;;;;;

;;  (map println (map render-row-to-str (map row-to-render-row rendered-grid))))

