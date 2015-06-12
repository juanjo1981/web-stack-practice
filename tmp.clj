;CRUD OPERATIONS
;
(defn create [request]
  get new date from request
  check the identity consistency
  save! entity in the data store
  response on success
  response on error
  )

(defn retrieve-show [request]
  get id from the entity we want to retrieve
  retrieve the entity from the data store
  response on success --> we put the values in the response
  response on error
  )

(defn retrieve-index [request]
  later
  )

(defn update [request]
  get new data from de request
  retrieve the entity we want to update
  update the values
  check identity consistency
  update! the data store
  response on success --> we put the new state on the response
  response on error 
  )

(defn delete [request]
  get the identification of entity we want to delete
  delete! de entity from the data store
  response on success
  response on error
  )
