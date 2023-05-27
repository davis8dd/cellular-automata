(ns cellular-automata.core
    (:gen-class))

(require '[cellular-automata.graphics :as graphics]
         '[cellular-automata.input :as input])

(def rule-keys [[true true true]
                [true true false]
                [true false true]
                [true false false]
                [false true true]
                [false true false]
                [false false true]
                [false false false]])

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

(defn convert-str-to-type
    [constructor-fn input-string]
    (try (constructor-fn input-string)
         (catch NumberFormatException e (println "Please enter an integer."))))

(def str-to-int
    "Convert a string to an Integer, else throw an exception"
    (partial convert-str-to-type #(Integer. %)))

(defn pad-sides-with
    "Add supplied value to front and back of a sequence."
    [a-coll padding-value]
    (into [] (flatten
                 (concat [padding-value] [a-coll] [padding-value]))))

(defn create-initial-row
    "Create a starting row of odd width with one 'on' cell in center of row"
    [width]
    (let [row-width (str-to-int width)]
          (pad-sides-with true (repeat (- (/ row-width 2) (rem (+ width 1) 2)) false))))

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
    (pad-sides-with
        (calculate-next-generation
            a-vect
            3
            rule)
        false))

(defn calculate-grid
    "Calculate grid of specified length using either a supplied or default starting generation."
    ([rule num-of-rows initial-row]
        (loop [grid [initial-row] counter 1]
            (if (> counter num-of-rows)
                grid
                (recur (conj grid (into (vector) (get-next-row (last grid) rule))) (inc counter)))))
    ([rule num-of-rows]
        (calculate-grid rule num-of-rows [false false false false true false false false false])))

(defn interactive-cellular-automata
    "Prompt user for input and display corresponding cellular automoata"
    []
    (do (println (:greeting ui-messages))
        (let [rule (input/ask-input-with-message (:which-rule ui-messages) str-to-int)

              grid-width (input/ask-input-with-message (:grid-width ui-messages) str-to-int)
              grid-height (input/ask-input-with-message (:generations ui-messages) str-to-int)
              the-grid ()]
             (graphics/display-grid (graphics/with-border graphics/render-chars (graphics/rendered-grid (calculate-grid (create-rule rule) grid-height (create-initial-row grid-width))))))
        (println (:exiting ui-messages))))

(defn -main
    "Run interactive cellular automata function"
    [& args]
    (interactive-cellular-automata))
