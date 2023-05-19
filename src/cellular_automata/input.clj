(ns cellular-automata.input
    (:gen-class))
;
; (def validation-messages {:revalidate "Revalidate"})
;
; ;State
; {:user-input <string>
;  :state <symbol>}
;
; (def input-fsm {'Start {:init 'Ready}
;                 'Ready {:invalid-entry 'Invalid-Input}
;                 'Invalid-Input {:change-input 'Ready}
;                 'Read-Input {:receive-valid-input 'Success}})
;
; (defn next-state
;   "Update to the next state."
;   [app-state transition]
;   (let [new-state (get-in input-fsm [(:state app-state) transition])]
;     (assoc app-state :state new-state)))
;
; (defn handle-change-input
;   "Ask user for new input."
;   [app-state input]
;   (-> app-state
;       (assoc :user-input user-input)
;       (next-state :change-input)))
;
; (defn get-input
;   "Validate input."
;   [app-state]
;   (let [state (:state app-state)]
;     ()))
;
; (defn validate-input
;   "Get input from a user."
;   [app-state validation-function]
;   (let [state (:state app-state)]
;     (== true (validation-function :user-input))
;       (next-state :state :receive-valid-input)
;       (next-state :state :invalid-entry)))
;
; ;;;;;;;;;;;;;;;;;;;;;;;

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

; (defn get-valid-function
;     "stuff"
;     [validation-function]
;     (let [user-prompt "Ask a question"
;           repeat-message "Invalid, try again"
;           valid-value false]
;     ;(do (ask-input-with-message user-prompt)
;     (do (ask-input-with-message :revalidate)
;         (cond (= valid-value false)
;               (do (println "istrue")
;                   true)
;               (do (println "isfalse")
;                   false)
;               ))))
