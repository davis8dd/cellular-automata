(ns cellular-automata.input
    (:gen-class))

 (defn ask-input
     "Ask for input, optionally returning a default value.  May default to nil, indicating no input and no default value."
     ([]
         (ask-input nil))
     ([default-value]
         (let [input-value (read-line)]
             (if (empty? input-value)
                 default-value
                 input-value))))

 (defn ask-input-with-message
     "Prompt user for input with message, optionally display default value."
     ([message]
         (do (println message)
             (ask-input)))
     ([message converter-fn]
         (do (println (str message  ":"))
             (converter-fn (ask-input))))
     ([message converter-fn default-value]
         (do (println (str message  ": [" default-value "]"))
             (converter-fn (ask-input default-value)))))
