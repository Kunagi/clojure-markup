(ns kunagi.markup.markdown-test
  (:require
   [kunagi.markup.markdown :as sut]
   [clojure.test :refer [deftest testing is]]))

(deftest render

  (testing "empty"
    (is (= "" (sut/markdown nil)))
    (is (= "" (sut/markdown '()))))

  (testing "simple text"
    (is (= "hello world"
           (sut/markdown "hello world"))))

  (testing "multiple texts"
    (is (= "helloworld"
           (sut/markdown '("hello" "world")))))

  (testing "paragraph"
    (is (= "hello world\n\n"
           (sut/markdown [:p "hello world"]))))

  (testing "two paragraphs"
    (is (= "paragraph 1\n\nparagraph 2\n\n"
           (sut/markdown '([:p "paragraph 1"]
                           [:p "paragraph 2"])))))

  (testing "section with paragraph"
    (is (= "# Header\n\ncontent\n\n"
           (sut/markdown '([:section
                            {:title "Header"}
                            [:p "content"]])))))

  (testing "nested sections"
    (is (= "# Title\n\n## Subtitle\n\ncontent\n\n"
           (sut/markdown '([:section
                            {:title "Title"}
                            [:section
                             {:title "Subtitle"}
                             [:p "content"]]])))))


  #_(testing "header"
      (is (= "# Header"
             (sut/markdown [:h1 "Header"])))))
