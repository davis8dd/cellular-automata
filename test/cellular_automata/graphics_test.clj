(ns cellular-automata.graphics-test
  (:require [clojure.test :refer :all]
            [cellular-automata.graphics :refer :all]))

(deftest test-string-repeating-char-given-character-and-times-to-repeat-expect-character-repeated-that-many-times
  (testing "Test that the given character is repeated in a string the specified number of times"
    (let [a-char \a
          times-to-repeat 5
          expected-result "aaaaa"]
      (is (= (string-repeating-char a-char times-to-repeat) expected-result)))))

(deftest test-create-initial-row-given-row-width-expect-row-with-single-middle-value-set-to-true
  (testing "Test create a row of false boolean values, with the exception of the middle which is 'on'"
    (let [row-width 7
          expected-result [false false false true false false false]]
      (is (= (create-initial-row row-width) expected-result)))))

(deftest test-pad-sides-with-given-char-expect-padded-sequence
  (testing "Test that padding a sequence with a character returns the expected padded sequence."
    (let [a-seq [1 2 3]
          padding-value \#
          expected-result [\# 1 2 3 \#]]
      (is (= (pad-sides-with a-seq padding-value) expected-result)))))

(deftest test-pad-sides-with-value-given-sequence-padding-expect-padding-with-sequence
  (testing "Test that padding a sequence with a sequence returns the expected padded sequence."
    (let [input-value [1 2 3]
          expected-result [0 0 1 2 3 0 0]]
      (is (= (pad-sides-with input-value [0 0]) expected-result)))))

(deftest test-display-3x3-grid-of-single-character
  (testing "Render 3x3 grid"
    (let [character-to-render \x
          grid-to-render [[character-to-render character-to-render] [character-to-render character-to-render] [character-to-render character-to-render]]
          expected-value [[(get-in render-chars [:border :top-left]) (get-in render-chars [:border :top]) (get-in render-chars [:border :top]) (get-in render-chars [:border :top-right])]
                          [(get-in render-chars [:border :left]) character-to-render character-to-render (get-in render-chars [:border :right])]
                          [(get-in render-chars [:border :left]) character-to-render character-to-render (get-in render-chars [:border :right])]
                          [(get-in render-chars [:border :left]) character-to-render character-to-render (get-in render-chars [:border :right])]
                          [(get-in render-chars [:border :bottom-left]) (get-in render-chars [:border :bottom]) (get-in render-chars [:border :bottom]) (get-in render-chars [:border :bottom-right])]]]
      (is (= expected-value (with-border render-chars grid-to-render))))))
