(ns cellular-automata.graphics-test
  (:require [clojure.test :refer :all]
            [cellular-automata.graphics :refer :all]))

(deftest test-pad-sides-with-value-given-zero-padding-expect-padding
  (testing "That invalid input is rejected"
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
