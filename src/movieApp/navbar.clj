(ns movieApp.navbar
  (:require [compojure.core :refer :all]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [hiccup.bootstrap.page :refer :all]
            ))


(defn nav []
  [:nav {:class "navbar navbar-expand-lg navbar-light bg-light"}
   [:a {:class "navbar-brand" :href "/"} "Movies"]
   [:button {:type "button" :class "navbar-toggler" :data-toggle "collapse"
             :data-target "#navbarNavDropdown" :aria-expanded "false" :aria-controls "navbarNavDropdown"
             :aria-label "Toggle navigation"}
    [:span {:class "navbar-toggler-icon"}]]
   [:div {:class "collapse navbar-collapse" :id "navbarNavDropdown"}
    [:ul {:class "navbar-nav"}
     [:li {:class "nav-item dropdown"}
      [:a {:class "nav-link dropdown-toggle" :href "/" :id "navbarDropdownMenuLink" :data-toggle "dropdown" :aria-hashpopup "true" :aria-expanded "false"} "Movies"]
      [:div {:class "dropdown-menu" :aria-labelledby "navbarDropdownMenuLink"}
       [:a {:class "dropdown-item" :href "/"} "List Of Movies"]
       [:a {:class "dropdown-item" :href "/create"} "Add New Movie"]
       ]
      ]
    ]
]
    ])