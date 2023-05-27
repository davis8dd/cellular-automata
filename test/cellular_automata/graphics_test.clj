(ns cellular-automata.graphics-test
  (:require [clojure.test :refer :all]
            [cellular-automata.graphics :refer :all]))

(deftest test-string-repeating-char-given-character-and-times-to-repeat-expect-character-repeated-that-many-times
  (testing "Test that the given character is repeated in a string the specified number of times"
    (let [a-char \a
          times-to-repeat 5
          expected-result "aaaaa"]
      (is (= (string-repeating-char a-char times-to-repeat) expected-result)))))

(deftest test-row-to-render-row-given-boolean-sequence-expect-symbol-sequence-returned
  (testing "asdf"
    (let [input-value [true false true]
          expected-result [:on :off :on]]
      (is (= (row-to-render-row input-value) expected-result)))))

(deftest test-render-row-to-string-given-????-expect???
  (testing "Render array of characters given character keys as input."
    (let [input-value [:on :off :on]
          expected-result (str (get-in render-chars [:cells :on]) (get-in render-chars [:cells :off]) (get-in render-chars [:cells :on]))]
      (is (= (render-row-to-string input-value) expected-result)))))

(deftest test-grid-to-render-of-single-character-given-border-expect-3x3-grid-with-border
  (testing "Render single character with a border, returning a 3x3 grid"
    (let [character-to-render \x
          grid-to-render [[character-to-render character-to-render] [character-to-render character-to-render] [character-to-render character-to-render]]
          expected-value [[(get-in render-chars [:border :top-left]) (get-in render-chars [:border :top]) (get-in render-chars [:border :top]) (get-in render-chars [:border :top-right])]
                          [(get-in render-chars [:border :left]) character-to-render character-to-render (get-in render-chars [:border :right])]
                          [(get-in render-chars [:border :left]) character-to-render character-to-render (get-in render-chars [:border :right])]
                          [(get-in render-chars [:border :left]) character-to-render character-to-render (get-in render-chars [:border :right])]
                          [(get-in render-chars [:border :bottom-left]) (get-in render-chars [:border :bottom]) (get-in render-chars [:border :bottom]) (get-in render-chars [:border :bottom-right])]]]
      (is (= expected-value (with-border render-chars grid-to-render))))))
