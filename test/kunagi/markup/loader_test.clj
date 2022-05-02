(ns kunagi.markup.loader-test
  (:require
   [kunagi.markup.loader :as sut]
   [clojure.test :refer [deftest testing is]]))

(deftest load-test

  (testing "empty markup"
    (is (= '() (sut/load nil)))
    (is (= '() (sut/load '()))))

  (testing "simple text"
    (is (= '("simple text")
           (sut/load "simple text"))))

  (testing "multiple text elements"
    (is (= '("some text" "more text")
           (sut/load (list "some text" "more text")))))

  (testing "single element"
    (is (= '({:tag :dummy
              :children ("some text")})
           (sut/load [:dummy "some text"]))))

  (testing "multiple elements"
    (is (= '({:tag :dummy
              :children ("some text")}
             {:tag :dummy
              :children ("more text")})
           (sut/load '([:dummy "some text"]
                       [:dummy "more text"])))))

  (testing "nested elements"
    (is (= '({:tag :parent
              :children ("parent"
                         {:tag      :child
                          :children ("child")})})
           (sut/load '([:parent
                        "parent"
                        [:child "child"]])))))

  (testing "sections"
    (is (= '({:tag :section
              :title "Title"
              :section-depth 1
              :children ({:tag :section
                          :title "Subtitle"
                          :section-depth 2
                          :children ("content")})})
           (sut/load '([:section
                        {:title "Title"}
                        [:section
                         {:title "Subtitle"}
                         "content"]]))))))
