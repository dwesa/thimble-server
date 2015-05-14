(ns thimble-server.data-access-layer.neo4j.follow
    (:require [clojurewerkz.neocons.rest :as nr]
              [clojurewerkz.neocons.rest.relationships :as nrl]
              [clojurewerkz.neocons.rest.nodes :as nn]
              [clojurewerkz.neocons.rest.cypher :as cy]))

;; this is not evaluated at compile time because the neo4j server might not
;; be alive to revieve connection. Leve this is a string, using let in each Fn
(def host "http://localhost:7474/db/data/")

(defn get-node-from-username
    "takes a string username, sends request to neo4j based on username
    returns neo4j node obj
    @param      username    [string]    the user we are looking for
    returns     [node]"
    [username]
    (let [conn (nr/connect host)
          ;; is there a way to put our queries all in one folder?
          query (str "MATCH (p { username: '" username "' }) RETURN id(p)")
          query-resp (cy/query conn query)]
          (nn/get conn (get (get (:data query-resp) 0) 0))))

(defn follow
    "a function that takes usernames for a follower, and a user to be followed
    then creates the appropriate edge relationship request, sends
    it to neo4j, and intereperets the response
    @param  follower    [string]  user who wants to follow someone else
    @param  following   [string]  who they want to begin following
    returns             [?]       result of neo4j request"
    [follower following]
    (let [conn (nr/connect host)
          user1 (get-node-from-username follower)
          user2 (get-node-from-username following)]
	(nrl/create conn user1 user2 :follows)))
