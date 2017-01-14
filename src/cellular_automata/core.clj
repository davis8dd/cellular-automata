(ns cellular-automata.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Business Logic
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Try storing bits as boolean values.  Comparisons will be made by comparing boolean arrays

;; (def front-things [[:off :off :off] [:on :off :off] [:off :on :off] [:off :off :on] [:on :on :off] [:on :off :on] [:off :on :on] [:on :on :on]])
(def rule-keys [[false false false] [true false false] [false true false] [false false true] [true true false] [true false true] [false true true] [true true true]])

(defn int-to-bool-array [an-int] (vec (boolean-array (map #(= 1 %) (map #(Integer. (str %)) (Integer/toBinaryString an-int))))))
(defn create-rule [an-int] (zipmap rule-keys (int-to-bool-array an-int))) ;; Return a set of (front-value, on/off) pairs for all front-things

(defn pad-with-2 [initial-coll padding-value] (vector (->> initial-coll (into padding-value) (into padding-value))))

(defn pad-with
    "Add supplied value to front and back of a sequence"
    [a-coll padding-value]
    (concat [padding-value] a-coll [padding-value]))

(defn create-initial-row [width] (let [init-row (pad-with [true] (repeat (/ width 2) false))] (if (even? width) init-row (drop-last init-row))))


;; For each cell in the existing row, create a 3-length sub-vector and get the corresponding generated cell for the new row
;(defn next-row [a-row rule] (reduce [next-row] (fn []) (boolean-array [false]) ())
;(let [a-row [0 1 2 3 4 5 6] subvec-size 4] (loop [counter 0] (if (< counter (- (count a-row) (dec subvec-size))) (do (println (str (subvec a-row counter (+ subvec-size counter)))) (recur (inc counter))))))
;(defn next-row [a-row subvec-size rule] (loop [counter 0] (if (< counter (- (count a-row) (dec subvec-size))) (do (println (str (subvec a-row counter (+ subvec-size counter)))) (recur (inc counter))))))
;(defn next-row [a-row subvec-size rule] (let [next-row []] (do (cons false next-row) (loop [counter 0] (if (< counter (- (count a-row) (dec subvec-size))) (do (cons (get rule (subvec a-row counter (+ subvec-size counter))) next-row) (recur (inc counter))))) (cons false next-row)) next-row))
;(defn next-row [a-row subvec-size rule] (loop [next-row [] counter 0] (if (< counter (- (count a-row) (dec subvec-size))) (do (into next-row (vector (get rule (subvec a-row counter (+ subvec-size counter))))) (recur next-row (inc counter))) next-row)))

(defn calculate-next-generation
    "Process a generation using a rule and return next generation"
    [a-row subvec-size rule]
    (loop [next-row [] counter 0]
        (if (> counter (- (count a-row) subvec-size))
	    next-row
	    (recur (into next-row (vector (get rule (subvec a-row counter (+ subvec-size counter))))) (inc counter)))))

(defn get-next-row
    "Return next row in grid by calculating new generation"
    [a-vect rule]
    (pad-with (calculate-next-generation a-vect 3 rule) false))

;; Inputting the number of generations, and an optional array of values for 1st generation
;; Outputs a grid of (arraywidth + 2), or calculate width based on final generation
;; Starting generation is optional, default is 1 true in center
(defn calculate-grid
    "Calculate grid of specified length using either a supplied or default starting generation"
    ([rule num-of-rows initial-row]
        (loop [grid [initial-row] counter 1]
	    (if (> counter num-of-rows)
	        grid
		(recur (conj grid (into (vector) (get-next-row (last grid) rule))) (inc counter)))))
    ([rule num-of-rows]
        (calculate-grid rule num-of-rows [false false false false true false false false false])))
;;      (calculate-grid rule num-of-rows (calculate-initial-row STUFF))))
 
;; Example of "final" product... should probably be part of unit test
;(calculate-grid (create-rule 137) 20)

(defn display-grid
    "Display rendered grid of cellular automata"
    [rendered-grid]
    (doseq [rendered-grid-row (vec rendered-grid)]
        (println rendered-grid-row)))

(defn cellular-automata [num-of-rows rule-number] (display-grid (calculate-grid (create-rule rule-number) num-of-rows)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Render functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; An example of displaying a grid.  This should probably be a unit test
;; This one looks neat!
;(display-grid (calculate-grid (create-rule 177) 50 [false false false false false false false false false false true false false false false false false false false false false]))


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

;;;; How many generations to display?  Three scenarios exist (the first two make assumptions based on immutable data):
;; 1) Each new generation is identical to the previous.  Stop after detecting n identical generations.
;; 2) The generations start repeating a pattern
;; 3) Reach a maximum number of generations and stop, regardless of any patterns

;;
(defn repeated-generations [numbers] ())

(defn generation-pattern [] ())

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
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; UI Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def ui-messages {:greeting "Hello!"
                  :grid-width "Please enter a grid width."
		  :generations "How many generations would you like to see?"
		  :initial-row "Do you want to set the initial generation?"
                  :which-rule "Which rule would you like to use?" ; Valid values are [0, 255]
                  :exiting "Goodbye!"})

(defn display-message [message-key] (println (message-key ui-messages)))

(defn ask-input ([] (ask-input nil))
                ([default-value] (let [input-value (read)] (if (empty? input-value) default-value input-value))))

(defn get-input-with-message
    "Prompt user for input with message, optionally display default value"
    ([message-key]
        (do (display-message message-key)
	    (ask-input)))
    ([message-key default-value]
        (do (println (str (message-key ui-messages) ": [" default-value "]"))
	    (ask-input default-value))))
;;                             ([message-key validation-fn default-value] (do (println (str (message-key ui-messages) " (" (apply str (map #(str % ",") valid-inputs)) ") : [" default-value "]")) (ask-input default-value))))

;;(defn run-ui [messages ui-messages] ())

(defn ask-grid-width
    "Ask user for desired grid width"
    [default-width]
    (get-input-with-message :grid-width default-width))

(defn ask-initial-row
    "Ask user for initial generation"
    [default-row]
    (get-input-with-message :initial-row (->> [false false true false false] row-to-render-row render-row-to-str)))
;;  (get-input-with-message :initial-row (->> (DEFAULT-ROW ???) row-to-render-row render-row-to-str)))

;(defn ask-generations [default-generations] (get-input-with-message :generations ))

;MOVE THIS TO THE PROPER SECTION
;(defn get-initial-row [INT] (create-initial-width)
;                      [bools[]] pass on)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; High-Level Control Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn start-game [] (do (display-message :greeting)
			(let [rule        (get-input-with-message :which-rule)
			      grid-width  (get-input-with-message :grid-width)
			      grid-height (get-input-with-message :generations)]
			     (display-grid (with-border render-chars (rendered-grid (calculate-grid (create-rule rule) grid-height (create-initial-row grid-width))))))
			(display-message :exiting)))
