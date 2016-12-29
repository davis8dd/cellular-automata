(ns cellular-automata.core-test
  (:require [clojure.test :refer :all]
            [cellular-automata.core :refer :all]))

;(deftest a-test
;  (testing "FIXME, I fail."
;    (is (= 0 1))))

(deftest verify-pad-with-use-cases
  (testing "All input types should be able to be padded using a single value or coll"
    (is (= '(false true false) (pad-with [true] false )))
    (is (= (pad-with [true] (repeat 2 false)) '(false false true false false)))
    (is (= (pad-with ["X"] "O") ("O" "X" "O")))))
