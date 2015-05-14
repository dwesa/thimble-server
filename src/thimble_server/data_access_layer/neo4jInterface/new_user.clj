(ns thimble-server.data-access-layer.neo4j.follow
    (:require [clojurewerkz.neocons.rest :as nr]
              [clojurewerkz.neocons.rest.relationships :as nrl]
              [clojurewerkz.neocons.rest.nodes :as nn]
              [clojurewerkz.neocons.rest.cypher :as cy]))

;; this is not evaluated at compile time because the neo4j server might not
;; be alive to revieve connection. Leve this is a string, using let in each Fn
(def host "http://localhost:7474/db/data/")

(defn create-user-node
   [username]
   "takes a username, creates new user node in neo4j.
    if user already exists, false is returned
    @param      username        [string]    new user
    returns     [boolean]       true/false depending on success"
   (let [conn  (nr/connect "http://localhost:7474/db/data/")]
     (nn/create conn {:username username :unique true})))
