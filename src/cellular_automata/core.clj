(ns cellular-automata.core
    (:gen-class))

(require '[cellular-automata.graphics :as graphics]
         '[cellular-automata.input :as input])
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Business Logic
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def rule-keys [[false false false]
                [true false false]
                [false true false]
                [false false true]
                [true true false]
                [true false true]
                [false true true]
                [true true true]])

(def ui-messages {:greeting "Welcome to the elementary cellular automata generator!"
                  :grid-width "Please enter a grid width."
                  :generations "How many generations would you like to see?"
                  :initial-row "Do you want to set the initial generation?"
                  :which-rule "Which rule would you like to use (Enter a number between [0, 255], or h for help)"
                  :exiting "Goodbye!"})

(defn pad-front-to-length-with
    "Add a number of padding values to the front of a sequence to make it the specified length."
    [a-coll final-length padding-value]
    (let [padding-size (- final-length (count a-coll))]
    (into [] (flatten
                 (concat (repeat padding-size padding-value) [a-coll])))))

(defn int-to-bool-array
    "Change integer to a boolean array to represent cell on/off states."
    [an-int]
    (pad-front-to-length-with (do (map #(= 1 %)
                                       (map #(Integer. (str %))
                                            (Integer/toBinaryString an-int)))) 8 false))

(defn create-rule
    "Create rule for processing next generation"
    [an-int]
    (cond
      (< an-int 0) nil
      (> an-int 255) nil
      :else (zipmap rule-keys (int-to-bool-array an-int)))) ;; Return a set of (front-value, on/off) pairs for all front-things

(defn calculate-next-generation
    "Process a generation using a rule and return next generation.
     The rule specifies each element in the next generation
     based on the current generation."
    [a-row subvec-size rule]
    (let [row-length (count a-row)]
    (loop [next-row [] counter 0]
        (if (> counter (- row-length subvec-size))
            next-row
            (recur (into next-row (vector (get rule (subvec a-row counter (+ subvec-size counter))))) (inc counter))))))

(defn get-next-row
    "Return next row in grid by calculating new generation."
    [a-vect rule]
    (graphics/pad-sides-with
        (calculate-next-generation
            a-vect
            3
            rule)
        false))

;; Inputting the number of generations, and an optional array of values for 1st generation
;; Outputs a grid of (arraywidth + 2), or calculate width based on final generation
;; Starting generation is optional, default is 1 true in center
(defn calculate-grid
    "Calculate grid of specified length using either a supplied or default starting generation."
    ([rule num-of-rows initial-row]
        (loop [grid [initial-row] counter 1]
            (if (> counter num-of-rows)
                grid
                (recur (conj grid (into (vector) (get-next-row (last grid) rule))) (inc counter)))))
    ([rule num-of-rows]
        (calculate-grid rule num-of-rows [false false false false true false false false false])))
 
;;;; If determining the maximum number of rows, how many generations should be displayed?  Three scenarios exist (the first two make assumptions based on immutable data):
;; 1) Each new generation is identical to the previous.  Stop after detecting n identical generations.
;; 2) The generations start repeating a pattern
;; 3) Reach a maximum number of generations and stop, regardless of any patterns

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; UI Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn convert-str-to-type
    [constructor-fn input-string]
    (try (constructor-fn input-string)
         (catch NumberFormatException e (println "Please enter an integer."))))

(def str-to-int
    "Convert a string to an Integer, else throw an exception"
    (partial convert-str-to-type #(Integer. %)))

(defn ask-grid-width
    "Ask user for desired grid width."
    [default-width]
    (input/ask-input-with-message (:grid-width ui-messages) str-to-int default-width))

(defn ask-initial-row
    "Ask user for initial generation."
    [default-row]
    (input/ask-input-with-message (:initial-row ui-messages) (->> [false false true false false]
                                              graphics/row-to-render-row
                                              graphics/render-row-to-str)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; High-Level Control Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn interactive-cellular-automata
    "Prompt user for input and display corresponding cellular automoata"
    []
    (do (println (:greeting ui-messages))
        (let [rule (input/ask-input-with-message (:which-rule ui-messages) str-to-int)

              grid-width (input/ask-input-with-message (:grid-width ui-messages) str-to-int)
              grid-height (input/ask-input-with-message (:generations ui-messages) str-to-int)
              the-grid ()]
             (graphics/display-grid (graphics/with-border graphics/render-chars (graphics/rendered-grid (calculate-grid (create-rule rule) grid-height (graphics/create-initial-row grid-width))))))
        (println (:exiting ui-messages))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; MAIN
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn -main
    "Run interactive cellular automata function"
    [& args]
    (interactive-cellular-automata))
