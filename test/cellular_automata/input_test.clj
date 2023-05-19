(ns cellular-automata.input-test
  (:require [clojure.test :refer :all]
            [cellular-automata.input :refer :all]))

(deftest test-ask-input-given-input-expect-input-returned
  (testing "When providing a default value and input given, expect input returned."
    (with-in-str "testinput"
      (let [function-return (ask-input "default-response")
            expected-value "testinput"]
        (is (= function-return expected-value))))))

(deftest test-ask-input-given-no-input-expect-default-returned
  (testing "When providing a default value and no input, expect default value returned."
    (with-in-str ""
      (let [function-return (ask-input "default-response")
            expected-value "default-response"]
        (is (= function-return expected-value))))))

(deftest test-ask-input-with-message-given-input-expect-default-returned
  (testing "When providing a default value and no input, expect default value returned."
    (with-in-str ""
      (let [function-return (ask-input "default-response")
            expected-value "default-response"]
        (is (= function-return expected-value))))))
