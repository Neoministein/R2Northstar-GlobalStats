/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: false,
    trailingSlash: true,
    basePath: '',
    publicRuntimeConfig: {
        contextPath:'',
        uploadPath: ''
    }
};

module.exports = nextConfig;
