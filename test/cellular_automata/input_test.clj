(ns cellular-automata.input-test
  (:require [clojure.test :refer :all]
            [cellular-automata.input :refer :all]))

(deftest test-ask-input-given-input-expect-input-returned
  (testing "When providing a default value and input given, expect input returned."
    (with-in-str "testinput"
      (let [default-value "default-response"
            expected-value "testinput"]
        (is (= (ask-input default-value) expected-value))))))

(deftest test-ask-input-given-no-input-expect-default-returned
  (testing "When providing a default value and no input, expect default value returned."
    (with-in-str ""
      (let [input-value "default-response"]
        (is (= (ask-input input-value) input-value))))))

(deftest test-ask-input-with-message-given-response-expect-value-returned
  (testing "When prompted and input provided, expect input returned."
    (with-in-str "abc"
      (let [expected-value "abc"]
        (is (= (ask-input-with-message "amessage") expected-value))))))

(deftest test-ask-input-with-message-given-response-to-int-expect-int-value-returned
  (testing "When prompted and input transformed via function, expect transformed value returned."
    (with-in-str "123"
      (let [expected-value 123]
        (is (= (ask-input-with-message "amessage" #(Integer. %)) expected-value))))))

(deftest test-ask-input-with-message-and-default-given-no-response-expect-default-as-int-value-returned
  (testing "When prompted and default value transformed via function, expect transformed value returned."
    (with-in-str ""
      (let [default-value "234"
            expected-value 234]
        (is (= (ask-input-with-message "amessage" #(Integer. %) default-value) expected-value))))))
