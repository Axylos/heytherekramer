const express = require('express');
const logger = require('morgan');
const bodyParser = require('body-parser');
const pg = require("pg");
const axios = require("axios");
require("dotenv").config();

const app = express();
app.use(bodyParser.json());

const { Pool, Client } = require("pg");
const user = process.env.DB_USER;
const password = process.env.DB_PASS;
const pool = new Pool({
  user,
  password,
  host: '127.0.0.1',
  database: "kramer"
});

const BASE_URL = "https://github.com/login/oauth/authorize";
const TOKEN_URL = "https://github.com/login/oauth/access_token"; 

function genRedirectUrl(id) {
  const params = new URLSearchParams();
  const uri = new URL(BASE_URL);
  params.set("client_id", process.env.CLIENT_ID);
  params.set("redirect_uri", process.env.REDIRECT_URI)
  const scope = "read:org notifications read:user read:discussion"
  params.set("scope", scope);
  params.set("client_secret", process.env.CLIENT_SECRET)
  params.set("state", id);

  uri.search = params;
  return uri;
}

async function saveCreds(code, id) {

}

app.get("/", async (req, res) => {
  try {

    const result = await pool.query(
    "INSERT INTO tokens (token) VALUES (null) RETURNING id"
    );
    const id = result.rows[0].id
    const uri = genRedirectUrl(id);
    console.log(uri);
    res.redirect(uri);
  } catch (err) {
    console.log(err);
    res.sendStatus(500);
  }
})

app.get("/oauthcb", async (req, res) => {
  const { code, state } = req.query;
  const uri = new URL(TOKEN_URL);
  const params = new URLSearchParams()
  params.set("client_id", process.env.CLIENT_ID);
  params.set("client_secret", process.env.CLIENT_SECRET);
  params.set("code", code);
  params.set("state", state);
  params.set("redirect_uri", process.env.REDIRECT_URI);
  uri.search = params;
  console.log(uri.toString());

  try {
    const resp = await axios.post(uri.toString(), null, {
      headers: {
        "Accept": "application/json"
      }
    });

    console.log(resp.data)
    const { access_token } = resp.data;
    await pool.query(
      `
      UPDATE tokens
      SET token = $1
      WHERE id = $2
      `, [access_token, state]
    );
    console.log(access_token, state);

    res.json({
      access_token,
      state,
      code,
    });
  } catch (err) {
    console.log(err);
    res.sendStatus(500)
  }
})


app.listen(3020, () => console.log("up and running"));
