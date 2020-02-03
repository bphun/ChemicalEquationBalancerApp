package main

import (
	"encoding/json"
    "fmt"
    "log"
    "net/http"
)

type WebhookPushData struct {
	pushed_at int32
	tag	string
	pusher string
}

type Repository struct {
	status string
	description string
	is_trusted bool
	full_description string
	repo_url string
	owner string
	is_official bool
	is_private bool
	name string
	namespace string
	repo_name string
}

type WebhookRequestBody struct {
	push_data WebhookPushData
	repository Repository
	callback_url string	`json:"callback_url"`
}

func handler(w http.ResponseWriter, r *http.Request) {
	var webhookRequestBody WebhookRequestBody

	err := json.NewDecoder(r.Body).Decode(&webhookRequestBody)
	if err != nil {
		http.Error(w, "JSON Error: " + err.Error(), http.StatusBadRequest)
		fmt.Println("JSON Error: %s", err.Error())
		return
	}

	log.Println(webhookRequestBody)
	
	fmt.Println("Success")

	fmt.Fprintf(w, "hi\n")
}

func main() {
	mux := http.NewServeMux()

    mux.HandleFunc("/hooks", handler)

	err := http.ListenAndServe(":8081", mux)
	log.Fatal(err)
}
