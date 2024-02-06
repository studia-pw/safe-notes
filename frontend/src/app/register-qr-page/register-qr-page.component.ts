import { Component, OnInit } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { AuthServiceService } from '../services/auth-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-register-qr-page',
  standalone: true,
  imports: [NgOptimizedImage, MatButtonModule, MatIconModule],
  templateUrl: './register-qr-page.component.html',
  styleUrl: './register-qr-page.component.css',
})
export class RegisterQrPageComponent implements OnInit {
  qrCodeUrl!: string;
  private email!: string;

  constructor(
    private authService: AuthServiceService,
    private router: Router,
    private route: ActivatedRoute,
  ) {
    this.route.queryParams.subscribe((params) => {
      this.email = params['email'];
    });
  }

  ngOnInit() {
    this.authService.get2faQRCode(this.email).subscribe(
      (qrCode) => {
        this.qrCodeUrl = qrCode.qrUrl;
      },
      (error) => {
        console.log('Error getting QR code: ', error);
      },
    );
  }

  onButtonClicked() {
    this.router.navigate(['/login']);
  }
}
