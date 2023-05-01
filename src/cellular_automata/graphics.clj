(ns cellular-automata.graphics
    (:gen-class))

(def render-chars {:cells {:on (char 9607)
                           :off (char 9617)}
                           :border {:top (char 9552)
                           :bottom (char 9552)
                           :left (char 9553)
                           :right (char 9553)
                           :top-right (char 9559)
                           :bottom-right (char 9565)
                           :top-left (char 9556)
                           :bottom-left (char 9562)}})

(defn pad-sides-with
    "Add supplied value to front and back of a sequence."
    [a-coll padding-value]
    (into [] (flatten
                 (concat [padding-value] [a-coll] [padding-value]))))

(defn create-initial-row
    "Create a starting row of specified width with one 'on' cell in center of row"
    [width]
    (let [init-row (pad-sides-with true (repeat (/ width 2) false))]
        (if (> (count init-row) width)
            (into [] (drop 1 init-row))
            init-row)))

(defn calculate-next-generation
    "Process a generation using a rule and return next generation."
    [a-row subvec-size rule]
    (loop [next-row [] counter 0]
        (if (> counter (- (count a-row) subvec-size))
            next-row
            (recur (into next-row (vector (get rule (subvec a-row counter (+ subvec-size counter))))) (inc counter)))))

(defn get-next-row
    "Return next row in grid by calculating new generation."
    [a-vect rule]
    (pad-sides-with
        (calculate-next-generation
            a-vect
            3
            rule)
        false))

(defn display-grid
    "Display rendered grid of cellular automata."
    [rendered-grid]
    (doseq [rendered-grid-row rendered-grid]
        (println (apply str rendered-grid-row))))

(defn string-repeating-char
    "Repeat supplied character"
    [a-char times]
    (apply str (repeat times a-char)))

(defn add-border-top
    "Add top border"
    [render-view-chars border-width]
        (let [top-left-char (get-in render-view-chars [:border :top-left])
              top-right-char (get-in render-view-chars [:border :top-right])
              top-char (get-in render-view-chars [:border :top])]
                (concat (vector top-left-char)
                 (repeat border-width top-char)
                 (vector top-right-char))))

(defn add-border-sides-to-row
    "Add border sides to single row"
    [render-view-chars render-row]
        (let [right-char (get-in render-view-chars [:border :right])
              left-char (get-in render-view-chars [:border :left])]
                  (concat (vector left-char) render-row (vector right-char))))
                 ;(apply str (concat (str left-char)
                 ;                   render-row
                 ;                   (str right-char)))))

(defn add-border-sides
    "Add border sides to group of rows"
    [render-view-chars render-grid]
        (let [right-char (get-in render-view-chars [:border :right])
              left-char (get-in render-view-chars [:border :left])]
                 ;(reduce (fn [bordered-grid render-row] (conj bordered-grid (add-border-sides-to-row render-chars render-row))) []  render-grid)))
                 (reduce (fn [bordered-grid render-row] (conj bordered-grid (add-border-sides-to-row render-chars render-row))) []  render-grid)))

(defn add-border-bottom
    "Add border bottom"
    [render-view-chars border-width]
        (let [bottom-left-char (get-in render-view-chars [:border :bottom-left])
              bottom-right-char (get-in render-view-chars [:border :bottom-right])
              bottom-char (get-in render-view-chars [:border :bottom])]
                (concat (vector bottom-left-char)
                 (repeat border-width bottom-char)
                 (vector bottom-right-char))))


(defn with-border
    "Display border of characters defined by 'render-chars' around content"
    [render-chars content]
        (let [content-width (count (first content))]
                (-> [(add-border-top render-chars content-width)]
                    (into (add-border-sides render-chars content))
                    (into [(add-border-bottom render-chars content-width)]))))

;;;;;;;;;;;;;;;;;;;;;;;;;

(defn row-to-render-row
    "Take a generation and return a list consumable by the View functions."
    [row]
    (reduce (fn [render-row cell] (if cell (conj render-row :on) (conj render-row :off))) [] row))

(defn render-row-to-str
    "Take a View-friendly generation and return a string ready for display rendering."
    [render-row]
    (apply str (map #(get (get render-chars :cells) %) render-row)))

;(defn rendered-grid [grid] (map render-row-to-str (map row-to-render-row grid)))

(defn rendered-grid
    [grid] (->> grid
                (map row-to-render-row)
                (map render-row-to-str)))
