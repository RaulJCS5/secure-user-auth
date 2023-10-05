import React, { useState } from 'react';

export const Contact = () => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        message: '',
    });

    const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        // Replace 'your_email@example.com' with your actual email address
        const toEmail = 'rauljosesantos@hotmail.com';

        // Compose the email subject and body
        const subject = `Message from ${formData.name}`;
        const body = `Name: ${formData.name}\nEmail: ${formData.email}\n\n${formData.message}`;

        // Create a "mailto" link to open the user's default email client
        const mailtoLink = `mailto:${toEmail}?subject=${encodeURIComponent(
            subject
        )}&body=${encodeURIComponent(body)}`;

        // Open the user's default email client with the composed email
        window.location.href = mailtoLink;
    };

    return (
        <div>
            <h1>Contact</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="name">Name:</label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="message">Message:</label>
                    <textarea
                        id="message"
                        name="message"
                        value={formData.message}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <button type="submit">Send Email</button>
                </div>
            </form>
        </div>
    );
};
