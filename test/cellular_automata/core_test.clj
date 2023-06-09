(ns cellular-automata.core-test
  (:require [clojure.test :refer :all]
            [cellular-automata.core :refer :all]))

(deftest test-create-rule-with-int-less-than-0
  (testing "Validate boolean array generated by a rule."
    (is (= nil (create-rule -1)))))

(deftest test-create-rule-with-int-greater-than-255
  (testing "Validate boolean array generated by a rule."
    (is (= nil (create-rule 256)))))

(deftest test-create-rule-with-valid-int
  (testing "Validate boolean array generated by a rule."
    (let [expected-value {[true true true] false,
                          [true true false] true,
                          [true false true] true,
                          [true false false] false,
                          [false true true] true,
                          [false true false] true,
                          [false false true] true,
                          [false false false] false}]
      (is (= expected-value (create-rule 110))))))

(deftest test-pad-sides-with-given-char-expect-padded-sequence
  (testing "Test that padding a sequence with a character returns the expected padded sequence."
    (let [a-seq [1 2 3]
          padding-value \#
          expected-result [\# 1 2 3 \#]]
      (is (= (pad-sides-with a-seq padding-value) expected-result)))))

(deftest test-pad-sides-with-given-sequence-padding-expect-padding-with-sequence
  (testing "Test that padding a sequence with a sequence returns the expected padded sequence."
    (let [input-value [1 2 3]
          expected-result [0 0 1 2 3 0 0]]
      (is (= (pad-sides-with input-value [0 0]) expected-result)))))

(deftest test-int-to-bool-array-with-valid-8-bit-int-expect-bool-flag-array
  (testing "Passing a valid int generates a boolean array matching the integer as bits."
    (let [expected-value [true true true false true false false true]]
      (is (= (int-to-bool-array 233) expected-value)))))

(deftest test-int-to-bool-array-with-valid-6-bit-int-expect-8-bit-bool-flag-array
  (testing "Passing a valid int generates a boolean array matching the integer as bits."
    (let [expected-value [false false true false false false true false]]
      (is (= (int-to-bool-array 34) expected-value)))))

(deftest test-create-initial-row-given-row-width-expect-row-with-single-middle-value-set-to-true
  (testing "Test create a row of false boolean values, with the exception of the middle which is 'on'."
    (let [row-width 7
          expected-result [false false false true false false false]]
      (is (= (create-initial-row row-width) expected-result)))))

(deftest test-create-initial-row-with-even-length-expect-one-less-length-and-single-middle-value-set-to-true
  (testing "Test create an odd row of false boolean values, removing one from an even-length row if necessary."
    (let [row-width 8
          expected-result [false false false true false false false]]
      (is (= (create-initial-row row-width) expected-result)))))


(deftest test-pad-front-to-length-with-8-given-empty-and-space-expect-8-spaces
  (testing "Expect a thing.")
    (let [initial-array [1 2 3]
          padding 0
          final-size 5
          expected-value [0 0 1 2 3]]
      (is (= (pad-front-to-length-with initial-array final-size padding) expected-value))))

(deftest test-calculate-next-generation-given-valid-rule-expect-next-row-generated-from-rule
  (testing "That the passed rule is used to generate the next row."
    (let [a-vect (vector false false false true false false false)
          subvec-size 3
          rule {[false false false] true,
                [true false false] false,
                [false true false] false,
                [false false true] true,
                [true true false] true,
                [true false true] false,
                [false true true] true,
                [true true true] true}
          expected-result [true true false false true]]
      (is (= (calculate-next-generation a-vect subvec-size rule) expected-result)))))

(deftest test-calculate-grid-given-valid-rule-and-row-expect-calculated-grid
  (testing "That a valid rule and initial generation generates a grid."
    (let [valid-rule {[false false false] true,
                [true false false] false,
                [false true false] false,
                [false false true] true,
                [true true false] true,
                [true false true] false,
                [false true true] true,
                [true true true] true}
          number-of-rows 3
          initial-row [false false true false false]
          expected-result [[false false true false false]
                           [false true false false false]
                           [false false false true false]
                           [false true true false false]]]
      (is (= (calculate-grid valid-rule number-of-rows initial-row) expected-result)))))

(deftest test-str-to-int-given-int-in-str-expect-int-of-same-value
  (testing "Passing an integer as a string returns that integer."
    (let [input-value "123"
          expected-result 123]
      (is (= (str-to-int input-value) expected-result)))))

(deftest test-interactive-cellular-automata-given-valid-input-expect-no-errors
  (testing "That a valid rule and user input generates a grid."
    (with-in-str "20\n3\n3"
      (let [expected-result nil]
       (is (= (interactive-cellular-automata) expected-result))))))
