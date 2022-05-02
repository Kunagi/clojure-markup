(ns kunagi.markup.loader)

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
                 opts (assoc :opts opts))]
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

(defn load
  [markup]
  (load-markup markup))

(comment)
