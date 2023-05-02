<template>
  <div class="col-md-12" style="width:600px; margin-left: auto; margin-right: auto;">
  <div style="text-align: center;">
    <h2> Update Your Profile</h2>
    <img :src="currentUser.picture" style="width:100px; margin-left:auto; margin-right:auto;">
  </div>
    <div>
      <Form @submit="handleUpdate" :validation-schema="schema">
        <div v-if="!successful">
          <div class="form-group">
            <label for="name">Name</label>
            <Field name="name" type="text" class="form-control" :value="currentUser.name"/>
            <ErrorMessage name="name" class="error-feedback" />
          </div>
          <div class="form-group">
            <label for="email">Email</label>
            <Field name="email" type="text" class="form-control" :value="currentUser.email"/>
            <ErrorMessage name="email" class="error-feedback" />
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <Field name="password" type="password" class="form-control" placeholder="Leave blank if unchanged"/>
            <ErrorMessage name="password" class="error-feedback" />
          </div>

          <div class="form-group">
            <button class="btn btn-primary btn-block" :disabled="loading">
              <span
                v-show="loading"
                class="spinner-border spinner-border-sm"
              ></span>
              Save Changes
            </button>
          </div>
        </div>
      </Form>

      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";

export default {
  name: "Account",
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  
  data() {
    const schema = yup.object().shape({
      username: yup
        .string()
        .min(3, "Must be at least 3 characters!")
        .max(20, "Must be maximum 20 characters!"),
      name: yup
        .string()
        .min(3, "Must be at least 3 characters!")
        .max(20, "Must be maximum 20 characters!"),
      email: yup
        .string()
        .email("Email is invalid!")
        .max(50, "Must be maximum 50 characters!"),
      password: yup
        .string()
        .min(0)
        .max(40, "Must be maximum 40 characters!"),
    });
    return {
      successful: false,
      loading: false,
      message: "",
      schema,
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }
  },
  methods: {
    handleUpdate(user) {
      // [API]
      fetch(`https://localhost:8080/api/accounts/${this.currentUser.username}`, {
        method: 'PATCH',
        body: JSON.stringify({
          password: user.password,
          email: user.email,
          name: user.name
        }),
        headers: {
          'Content-type': 'application/json; charset=UTF-8',
        },
      }).then(async response => {
        const data = await response.json();
        if (!response.ok) {
          const error = (data && data.message) || response.status;
          return Promise.reject(error);
        }
        else{
          this.$router.push('/home');
        }
      }).catch(error => {
        this.errorMessage = error;
        console.error('There was an error!', error);
      });
    },
  },
};
</script>
