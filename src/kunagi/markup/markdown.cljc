(ns kunagi.markup.markdown
  "Render markup as Markdown."
  (:require
   [kunagi.markup.loader :as loader]
   [clojure.string :as str]))

(defmulti render-element
  (fn [element]
    (-> element :tag)))

(defn render-elements [elements]
  (->> elements
       (map (fn [element]
              (cond
                (nil? element) nil
                (string? element) element
                :else (render-element element))))
       (remove nil?)
       seq
       (apply str)))

(defmethod render-element :default [element]
  (render-elements (-> element :children)))

(defmethod render-element :section [element]
  (str (->> (repeat "#") (take (-> element :section-depth)) (apply str))
       " "
       (-> element :title)
       "\n\n"
       (render-elements (-> element :children))))

(defmethod render-element :p [element]
  (str (render-elements (-> element :children))
       "\n\n"))

(defn markdown [markup]
  (->> (loader/load markup)
       render-elements))

(comment
  (->> (loader/load "hello world")))
