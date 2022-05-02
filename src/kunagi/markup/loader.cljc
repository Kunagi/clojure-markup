(ns kunagi.markup.loader)

;; * Parsing

(declare load-markup)

(defn load-element [element]
  (cond
    (nil? element) nil

    (string? element)
    element

    (vector? element)
    (let [tag (first element)
          _ (when-not (keyword? tag)
              (throw (ex-info "Invalid element. First element of vector must be a keyword."
                              {:element element})))
          content (rest element)
          [opts children] (if (-> content first map?)
                            [(-> content first) (-> content rest)]
                            [nil content])
          node (cond-> {:tag tag
                        :children (load-markup children)}
                 opts (merge opts))]
      node)

    :else (throw (ex-info (str "Invalid element")
                          {:element element}))))

(defn load-markup
  [markup]
  (cond
    (nil? markup) '()

    (vector? markup)
    (list (load-element markup))

    (sequential? markup)
    (map load-element markup)

    (string? markup)
    (list markup)

    :else (throw (ex-info (str "Invalid markup")
                          {:markup markup}))))

;; * Analysing

(defn assoc-depth-to-sections [elements depth]
  (->> elements
       (map (fn [element]
              (cond-> element
                (-> element :tag (= :section)) (assoc :section-depth depth))
              (if (-> element :tag (= :section))
                (-> element
                    (assoc :section-depth depth))
                element)))))

(defn element--assoc-depth-to-sections [element depth]
  (if (string? element)
    element
    (let [section-tag?  (-> element :tag (= :section))
          children (-> element :children)
          child-depth (if section-tag? (inc depth) depth)
          children (->> children
                        (map (fn [child]
                               (element--assoc-depth-to-sections child child-depth)))
                        seq)]
      (cond-> element
        section-tag? (assoc :section-depth depth)
        children (assoc :children children)
        ))))

(defn analyse-elements [elements]
  (->> elements
       (map (fn [element]
              (element--assoc-depth-to-sections element 1)))))

;; * Loading

(defn load
  [markup]
  (-> markup
      load-markup
      analyse-elements))

(comment)
